package priv.gsc.forum.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import priv.gsc.forum.ForumApplication;
import priv.gsc.forum.util.SensitiveFilter;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class SensitiveFilterTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void test1() {
        String text = "他喜欢赌博、还喜欢嫖娼，还经常吸毒，是个犯罪分子！";
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);
    }

    @Test
    public void test2() {
        String text = "他喜欢@赌@博@、还喜欢@嫖@娼@，还经常@吸@@@毒，是个犯@@@罪分子！";
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);
    }

}
