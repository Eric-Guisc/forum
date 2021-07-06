package priv.gsc.forum.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import priv.gsc.forum.entity.DiscussPost;

import java.util.List;

@Mapper
@Repository
public interface DiscussPostMapper {

    // 分页查询帖子，userId为可选的参数（不设置的时候即查询全部帖子，设置时为查询某用户的帖子）
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @Param注解用于给参数取别名
    // 如果只有一个参数，并且在<if>里使用，则必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);
}
