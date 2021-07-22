package priv.gsc.forum.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import priv.gsc.forum.entity.Message;

import java.util.List;

@Mapper
@Repository
public interface MessageMapper {

    // 查询当前用户的会话列表，每个会话只返回一条最新的私信
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户的会话数量
    int selectConversationCount(int userId);

    // 查询某个会话所包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);

    // 查询未读私信的数量（包括用户所有未读私信数量以及该用户某个会话未读私信数量）
    int selectLetterUnreadCount(int userId, String conversationId);
}
