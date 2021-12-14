package com.guli.edu.service.impl;

import com.guli.common.constants.PriceConstants;
import com.guli.common.exception.GuliException;
import com.guli.edu.entity.EduCourse;
import com.guli.edu.entity.EduCourseDescription;
import com.guli.edu.form.CourseInfoForm;
import com.guli.edu.mapper.EduCourseMapper;
import com.guli.edu.service.EduCourseDescriptionService;
import com.guli.edu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2021-06-01
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    private EduCourseDescriptionService courseDescriptionService;
    @Transactional
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        //保存课程基本信息
        EduCourse course = new EduCourse();
        course.setStatus(EduCourse.COURSE_DRAFT);
        BeanUtils.copyProperties(courseInfoForm,course);
        boolean resultCourseInfo = this.save(course);
        if(!resultCourseInfo){
            throw new GuliException(20001,"课程信息保存失败");
        }
        //保存课程详细信息
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        boolean resultDescription = courseDescriptionService.save(courseDescription);
        if(!resultDescription){
            throw new GuliException(20001,"课程详情信息保存失败");

        }
        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoFormById(String id) {
        EduCourse course = this.getById(id);
        if(course == null){
            throw new GuliException(20001,"数据不存在");

        }
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course,courseInfoForm);

        EduCourseDescription courseDescription = courseDescriptionService.getById(id);
        if(course != null){
            courseInfoForm.setDescription(courseDescription.getDescription());
        }
        // 设置显示精度，舍弃多余的位数
        courseInfoForm.setPrice(courseInfoForm.getPrice().setScale(PriceConstants.DISPLAY_SCALE, BigDecimal.ROUND_FLOOR));

        return courseInfoForm;
    }
}
