package com.guli.edu.controller.admin;

import com.guli.common.vo.R;
import com.guli.edu.entity.EduSubject;
import com.guli.edu.service.EduSubjectService;
import com.guli.edu.vo.SubjectNestedVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.logging.Logger;

@Api(description = "课程分类管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/edu/subject")
public class SubjectAdminController {

    @Autowired
    private EduSubjectService subjectService;

    @ApiOperation(value = "Excel批量导入")
    @PostMapping("import")
    public R addUser(
            @ApiParam(name = "file",value = "Excel文件",required = true)
            @RequestParam("file")MultipartFile file
            ){
        List<String> msg = subjectService.batchImpot(file);
        if(msg.size() == 0){
            return R.ok().message("批量导入成功");
        }else{
            return R.error().message("部分数据导入失败").data("messageList",msg);
        }
    }
    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("")
    public R nestedList(){
        List<SubjectNestedVo> subjectNestedVoList = subjectService.nestedList();
        return R.ok().data("items", subjectNestedVoList);

    }
    @ApiOperation(value = "根据id删除课程信息")
    @DeleteMapping("{id}")
    public R removeById(@ApiParam(name = "id",value = "课程id",required = true)
                        @PathVariable String id){
        subjectService.removeById(id);
        return R.ok();
    }
    @ApiOperation(value = "新增一级分类")
    @PostMapping("save-level-one")
    public R saveLevelOne(
            @ApiParam(name = "subject",value = "课程分类对象",required = true)
            @RequestBody EduSubject subject
            ){
        boolean result = subjectService.saveLevelOne(subject);
        if(result){
            return R.ok();
        }else{
            return R.error().message("添加失败");
        }
    }
    @ApiOperation(value = "新增二级分类")
    @PostMapping("save-level-two")
    public R saveLevelTwo(@ApiParam(name = "subject",value = "课程分类对象",required = true)
                          @RequestBody EduSubject subject){
        boolean result = subjectService.saveLevelTwo(subject);
        if(result){
            return R.ok();
        }else{
            return R.error().message("添加失败");
        }

    }
}
