package priv.gsc.forum.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import priv.gsc.forum.entity.User;
import priv.gsc.forum.service.UserService;
import priv.gsc.forum.util.ForumUtil;
import priv.gsc.forum.util.HostHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${forum.path.upload}")
    private String uploadPath;

    @Autowired
    private HostHolder hostHolder;

    @Value("${forum.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @GetMapping("/setting")
    public String getSettingPage() {
        return "site/setting";
    }

    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "site/setting";
        }

        // 获取文件名
        String fileName = headerImage.getOriginalFilename();
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式错误！");
            return "site/setting";
        }

        // 生成随机文件名
        fileName = ForumUtil.generateUUID() + suffix;
        // 确定文件存放位置
        File file = new File(uploadPath + "/" + fileName);

        try {
            // 存储文件
            headerImage.transferTo(file);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        // 更新当前用户的头像路径（Web访问路径）
        // http://localhost:8080/forum/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 头像实际存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);

        try(
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
                ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }

    }

}