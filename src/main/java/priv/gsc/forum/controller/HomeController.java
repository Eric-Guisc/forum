package priv.gsc.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import priv.gsc.forum.entity.DiscussPost;
import priv.gsc.forum.entity.Page;
import priv.gsc.forum.service.DiscussPostService;
import priv.gsc.forum.service.LikeService;
import priv.gsc.forum.service.MessageService;
import priv.gsc.forum.service.UserService;
import priv.gsc.forum.util.ForumConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements ForumConstant {

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page) {
        // Page的current、limit由前端传入(不传入，在Page中也有默认值)
        // rows、path需要设置
        // 方法调用前，SpringMVC会自动实例化Model和Page，并将Page注入到Model中，
        // 所以，在thymeleaf中可以直接访问Page对象中的数据。

        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                map.put("user",userService.findUserById(post.getUserId()));
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);

        return "index";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "/error/500";
    }
}
