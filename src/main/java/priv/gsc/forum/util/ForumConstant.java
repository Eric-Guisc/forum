package priv.gsc.forum.util;

public interface ForumConstant {

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认情况下登录凭证的保存时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;


    /**
     * 点击记住我后登录凭证的保存时间
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 10;

    /**
     * 帖子对应的EntityType
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 评论对应的EntityType
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 用户对应的EntityType
     */
    int ENTITY_TYPE_USER = 3;

    /**
     * 主题：评论
     */
    String TOPIC_COMMENT = "comment";

    /**
     * 主题：点赞
     */
    String TOPIC_LIKE = "like";

    /**
     * 主题：关注
     */
    String TOPIC_FOLLOW = "follow";

    /**
     * 系统用户ID
     */
    int SYSTEM_USER_ID = 1;

}
