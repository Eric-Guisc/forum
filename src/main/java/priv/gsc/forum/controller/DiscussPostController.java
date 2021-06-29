package priv.gsc.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import priv.gsc.forum.entity.DiscussPost;
import priv.gsc.forum.entity.User;
import priv.gsc.forum.service.DiscussPostService;
import priv.gsc.forum.service.UserService;
import priv.gsc.forum.util.ForumUtil;
import priv.gsc.forum.util.HostHolder;

import java.util.Date;

@Controller
@RequestMapping("/post")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;


    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return ForumUtil.getJSONString(403,"你还没有登录！");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        // 报错的情况，将来统一处理
        return ForumUtil.getJSONString(0, "发布成功！");
    }

    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model) {
        // 帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);

        // 用户
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);


        return "/site/discuss-detail";
    }
}
