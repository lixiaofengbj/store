package cn.store.service;


import cn.store.common.bean.ResponseBean;

public interface UserService {

    ResponseBean getUserList(String userName, Integer page, Integer pageSize);
}
