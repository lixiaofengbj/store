package cn.store.store_web.controller;

import cn.store.common.bean.ResponseBean;
import cn.store.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "USER_APIS")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("USER_LIST")
    @GetMapping("/userList")
    public ResponseBean getUserList(@ApiParam(name = "userName", value = "用户名") @RequestParam(required = false) String userName,
                                    @ApiParam(name = "page", value = "页数") @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @ApiParam(name = "pageSize", value = "条数") @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return userService.getUserList(userName,page,pageSize);
    }
}
