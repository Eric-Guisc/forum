package priv.gsc.forum.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import priv.gsc.forum.dao.LoginTicketMapper;
import priv.gsc.forum.dao.UserMapper;
import priv.gsc.forum.entity.LoginTicket;
import priv.gsc.forum.entity.User;
import priv.gsc.forum.util.ForumConstant;
import priv.gsc.forum.util.ForumUtil;
import priv.gsc.forum.util.MailClient;
import priv.gsc.forum.util.RedisKeyUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements ForumConstant {

    @Autowired
    private UserMapper userMapper;

    @Value("${forum.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;

//    @Autowired
//    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    public User findUserById(int id) {
//        User user = userMapper.selectById(id);
//        return user;

        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    public User findUserByName(String name) {
        User user = userMapper.selectByName(name);
        return user;
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空！");
            return map;
        }

        // 验证用户名
        User user1 = userMapper.selectByName(user.getUsername());
        if (user1 != null) {
            map.put("usernameMsg", "该用户名已经存在！");
            return map;
        }

        // 验证邮箱
        User user2 = userMapper.selectByEmail(user.getEmail());
        if (user2 != null) {
            map.put("emailMsg", "该邮箱已被注册！");
            return map;
        }

        // 注册用户
        user.setSalt(ForumUtil.generateUUID().substring(0,5));
        user.setPassword(ForumUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);    // 普通用户
        user.setStatus(0);  // 未激活
        user.setActivationCode(ForumUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        // url 为 http://localhost:8080/forum/activation/123/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(),"账号激活",content);


        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) // 重复激活
            return ACTIVATION_REPEAT;
        else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1); // 激活用户
            // 修改了user的状态，所以调用clearCache(userId)
            clearCache(userId);
            return ACTIVATION_SUCCESS;
        } else  // 激活失败
            return ACTIVATION_FAILURE;
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();
        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "用户名不能为空！");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在！");
            return map;
        }
        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活！");
            return map;
        }
        // 验证密码
        password = ForumUtil.md5(password + user.getSalt());
        if (!password.equals(user.getPassword())) {
            map.put("passwordMsg", "密码错误！");
            return map;
        }
        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(ForumUtil.generateUUID());
        loginTicket.setStatus(0);   // 有效
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
//        loginTicketMapper.insertLoginTicket(loginTicket);

        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
//        loginTicketMapper.updateStatus(ticket, 1);

        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    public LoginTicket findLoginTicket(String ticket) {
//        LoginTicket loginTicket = loginTicketMapper.selectByTicket(ticket);
//        return loginTicket;

        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    // 上传头像，修改user的headerUrl
    public void updateHeader(int userId, String headerUrl) {
        userMapper.updateHeaderUrl(userId, headerUrl);
        // 修改了user，所以调用clearCache(userId)
        clearCache(userId);
    }


    // 使用Redis缓存用户信息
    // 1、优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2、取不到时初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    // 3、数据变更时清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

}
