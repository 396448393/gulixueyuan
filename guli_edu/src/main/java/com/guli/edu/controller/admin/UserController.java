package com.guli.edu.controller.admin;

import com.guli.common.vo.R;
import com.guli.edu.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(description="用户登陆")
@RestController
@CrossOrigin //跨域
@RequestMapping("/user")
public class UserController {
    @ApiOperation(value = "用户登陆")
    @PostMapping("/login")
    public R longin(@RequestBody User user) {
        System.out.println(user.toString());
        return R.ok().code(20000).data("token",user.getUsername()+"-token");
    }
    @ApiOperation(value = "获取用户信息")
    @GetMapping("/info")
    public R getLoginInfo(@RequestParam("token") String token) {
        System.out.println(token);
        Map<String,Map<String,Object>> retMap=new HashMap<>();
        Map<String,Object> user = new HashMap<>();
        String[] roles={"admin"};
        user.put("roles",roles);
        user.put("introduction","I am a super administrator");
        user.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        user.put("name","Super Admin");
        retMap.put("admin-token",user);

        return R.ok().code(20000).data(user);
    }
    @ApiOperation(value = "用户注销")
    @PostMapping("/logout")
    public Map longinOut() {
        System.out.println("退出。。。");
        Map<String,String> ret=new HashMap<>();
        ret.put("code","20000");
        ret.put("data","success");
        return ret;
    }
}
