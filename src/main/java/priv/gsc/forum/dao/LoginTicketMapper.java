package priv.gsc.forum.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import priv.gsc.forum.entity.LoginTicket;

@Mapper
@Repository
@Deprecated     // 不推荐使用的注解
public interface LoginTicketMapper {

    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select * ",
            "from login_ticket where ticket = #{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}"
    })
    int updateStatus(String ticket, int status);
}
