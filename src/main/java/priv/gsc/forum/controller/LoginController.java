package priv.gsc.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import priv.gsc.forum.entity.User;
import priv.gsc.forum.service.UserService;
import priv.gsc.forum.util.ForumConstant;

import java.util.Map;

@Controller
public class LoginController implements ForumConstant {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String getRegisterPage() {
        return "site/register";
    }

    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, Object> registerMap = userService.register(user);
        if (registerMap == null || registerMap.isEmpty()) {
            model.addAttribute("msg", "注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
            model.addAttribute("target", "/index");
            return "site/operate-result";
        } else {
            model.addAttribute("usernameMsg",registerMap.get("usernameMsg"));
            model.addAttribute("passwordMsg",registerMap.get("passwordMsg"));
            model.addAttribute("emailMsg",registerMap.get("emailMsg"));
            return "site/register";
        }
    }

    @GetMapping("/activation/{userId}/{code}")
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int activation = userService.activation(userId, code);
        if (activation == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功，您的账号可以正常使用了！");
            model.addAttribute("target", "/login");
        } else if (activation == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "该账号已经激活过了");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败，您提供的激活码不正确！");
            model.addAttribute("target", "/index");
        }
        return "site/operate-result";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "site/login";
    }


}
