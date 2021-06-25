package priv.gsc.forum.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import priv.gsc.forum.entity.User;

@Mapper
@Repository
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeaderUrl(int id, String headerUrl);

    int updatePassword(int id, String password);
}
