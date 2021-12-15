package com.guli.edu.controller.admin;

import com.guli.common.vo.R;
import com.guli.edu.form.CourseInfoForm;
import com.guli.edu.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "课程管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/edu/coures")
public class CourseAdminController {
    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "新增课程")
    @PostMapping("save-course-info")
    public R saveCourseInfo(
            @ApiParam(name="CourseInfoForm",value = "课程基本信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm
            ){
        String courseId = courseService.saveCourseInfo(courseInfoForm);
        if(!StringUtils.isEmpty(courseId)){
            return R.ok().data("courseId",courseId);
        }else{
            return R.error().message("保存失败");
        }

    }
    @ApiOperation(value = "根据ID查询课程")
    @GetMapping("course-info/{id}")
    public R getById(
            @ApiParam(name="id",value = "课程ID",required = true)
            @PathVariable String id){
        CourseInfoForm courseInfoForm = courseService.getCourseInfoFormById(id);
        return R.ok().data("item",courseInfoForm);
    }
    @ApiModelProperty(value = "更新课程")
    @PutMapping("update-course-info/{id}")
    public R updateCourseInfoById(
            @ApiParam(name = "CourseInfoForm",value = "课程基本信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm,
            @ApiParam(name = "id",value = "课程ID",required = true)
            @PathVariable String id){
        courseService.updateCourseInfoById(courseInfoForm);
        return R.ok();
    }
}
