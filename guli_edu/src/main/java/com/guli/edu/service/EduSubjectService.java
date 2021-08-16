package com.guli.edu.service;

import com.guli.edu.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.vo.SubjectNestedVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author Helen
 * @since 2021-06-01
 */
public interface EduSubjectService extends IService<EduSubject> {

    List<String> batchImpot(MultipartFile file);

    List<SubjectNestedVo> nestedList();

    boolean saveLevelOne(EduSubject subject);

    boolean saveLevelTwo(EduSubject subject);
}
