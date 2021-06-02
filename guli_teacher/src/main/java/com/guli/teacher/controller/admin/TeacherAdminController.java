package com.guli.teacher.controller.admin;

import com.guli.common.vo.R;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description="讲师管理")
@RestController
@CrossOrigin //跨域
@RequestMapping("/admin/edu/teacher")
public class TeacherAdminController {
    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping
    public R list(){
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }

    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeById(@ApiParam(name = "id", value = "讲师ID", required = true)
                              @PathVariable String id){
        teacherService.removeById(id);
        return R.ok();
    }
}
