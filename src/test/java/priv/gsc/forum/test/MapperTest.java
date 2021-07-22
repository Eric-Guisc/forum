package priv.gsc.forum.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import priv.gsc.forum.ForumApplication;
import priv.gsc.forum.dao.DiscussPostMapper;
import priv.gsc.forum.dao.LoginTicketMapper;
import priv.gsc.forum.dao.MessageMapper;
import priv.gsc.forum.dao.UserMapper;
import priv.gsc.forum.entity.DiscussPost;
import priv.gsc.forum.entity.LoginTicket;
import priv.gsc.forum.entity.Message;
import priv.gsc.forum.entity.User;
import priv.gsc.forum.util.ForumUtil;
import sun.security.krb5.internal.Ticket;

import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(25);
        System.out.println(user);

        User zhangfei = userMapper.selectByName("zhangfei");
        System.out.println(zhangfei);

        User user1 = userMapper.selectByEmail("nowcoder127@sina.com");
        System.out.println(user1);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);

        rows = userMapper.updateHeaderUrl(150, "http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150, "hello");
        System.out.println(rows);
    }

    @Test
    public void selectPost() {
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(101, 0, 10);
        for (DiscussPost discussPost : discussPosts)
            System.out.println(discussPost);

        int count = discussPostMapper.selectDiscussPostRows(101);
        System.out.println(count);
    }

    @Test
    public void testLoginTicket() {
//        LoginTicket loginTicket = new LoginTicket();
//        loginTicket.setUserId(110);
//        loginTicket.setTicket(ForumUtil.generateUUID());
//        loginTicket.setStatus(0);
//        loginTicket.setExpired(new Date(System.currentTimeMillis() + 2 * 3600* 1000));
//        loginTicketMapper.insertLoginTicket(loginTicket);

        LoginTicket loginTicket = loginTicketMapper.selectByTicket("2761a4db74a44724a65828724408daae");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("2761a4db74a44724a65828724408daae", 1);
        loginTicket = loginTicketMapper.selectByTicket("2761a4db74a44724a65828724408daae");
        System.out.println(loginTicket);
    }

    @Test
    public void testInsertDiscussPost() {
        DiscussPost post = new DiscussPost();
        post.setUserId(13);
        post.setTitle("test11");
        post.setContent("content111");
        post.setStatus(0);
        post.setType(0);
        post.setCommentCount(0);
        post.setScore(0);
        post.setCreateTime(new Date());
        final int count = discussPostMapper.insertDiscussPost(post);
        System.out.println(count);
    }

    @Test
    public void testMessage() {
        List<Message> messages = messageMapper.selectConversations(111, 0, 20);
        for (Message message : messages)
            System.out.println(message);
        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        messages = messageMapper.selectLetters("111_112", 0, 20);
        for (Message message : messages)
            System.out.println(message);
        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        count  = messageMapper.selectLetterUnreadCount(111, null);
        System.out.println(count);
    }

}
