package priv.gsc.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.gsc.forum.dao.UserMapper;
import priv.gsc.forum.entity.User;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        User user = userMapper.selectById(id);
        return user;
    }
}
