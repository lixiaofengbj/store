package cn.store.service.impl;

import cn.store.common.bean.ResponseBean;
import cn.store.common.constant.ErrorEnum;
import cn.store.mapper.UserMapper;
import cn.store.model.User;
import cn.store.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseBean getUserList(String userName, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize, true);
        List<User> userList = this.userMapper.getUserList(userName);
        return new ResponseBean(ErrorEnum.SUCCESS, new PageInfo<User>(userList));
    }
}
