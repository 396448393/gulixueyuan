package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.edu.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.form.CourseInfoForm;
import com.guli.edu.query.CourseQuery;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Helen
 * @since 2021-06-01
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);
    CourseInfoForm getCourseInfoFormById(String id);
    void updateCourseInfoById(CourseInfoForm courseInfoForm);
    void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery);

}
