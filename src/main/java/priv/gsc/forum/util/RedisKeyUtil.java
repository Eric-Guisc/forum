package priv.gsc.forum.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)   使用set集合来存储赞的原因：既可以通过set算出点赞的数量，也可以通过set获取到每个点赞的人
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
}
