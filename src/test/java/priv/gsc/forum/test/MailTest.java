package priv.gsc.forum.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import priv.gsc.forum.ForumApplication;
import priv.gsc.forum.util.MailClient;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ForumApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("1091877410@qq.com","TEST","hello guisc!");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "Eric Gui");

        //  content是HTML形式
        String content = templateEngine.process("/mail/demo",context);
        System.out.println(content);

        mailClient.sendMail("1091877410@qq.com","HTML",content);
    }

}
