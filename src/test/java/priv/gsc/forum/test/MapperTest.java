package priv.gsc.forum.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import priv.gsc.forum.ForumApplication;
import priv.gsc.forum.dao.DiscussPostMapper;
import priv.gsc.forum.dao.UserMapper;
import priv.gsc.forum.entity.DiscussPost;
import priv.gsc.forum.entity.User;

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



}
