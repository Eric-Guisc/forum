package priv.gsc.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.gsc.forum.dao.DiscussPostMapper;
import priv.gsc.forum.entity.DiscussPost;

import java.util.List;

@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(userId, offset, limit);
        return discussPosts;
    }

    public int findDiscussPostRows(int userId) {
        int count = discussPostMapper.selectDiscussPostRows(userId);
        return count;
    }

}
