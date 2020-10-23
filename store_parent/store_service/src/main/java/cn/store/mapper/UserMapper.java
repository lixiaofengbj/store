package cn.store.mapper;

import cn.store.model.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    List<User> getUserList(@Param("userName") String userName);
}
