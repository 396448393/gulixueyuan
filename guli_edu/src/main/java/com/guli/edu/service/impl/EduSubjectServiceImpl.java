package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.util.ExcelImportUtil;
import com.guli.edu.entity.EduSubject;
import com.guli.edu.mapper.EduSubjectMapper;
import com.guli.edu.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.edu.vo.SubjectNestedVo;
import com.guli.edu.vo.SubjectVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2021-06-01
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public List<String> batchImpot(MultipartFile file) {
        List<String> msg = new ArrayList<>();
        try {
            ExcelImportUtil excelHSSFUtil = new ExcelImportUtil(file.getInputStream());
            Sheet sheet = excelHSSFUtil.getSheet();
            int rowCount = sheet.getPhysicalNumberOfRows();
            if(rowCount <= 1){
                msg.add("请填写数据");
                return msg;
            }
            for(int rowNum = 1; rowNum < rowCount;rowNum++){
                Row rowData = sheet.getRow(rowNum);
                if(rowData != null){
                    //一级分类名称
                    String levelOneValue = "";
                    Cell levelOneCell = rowData.getCell(0);
                    if(levelOneCell != null){
                        levelOneValue = excelHSSFUtil.getCellValue(levelOneCell);
                        if(StringUtils.isEmpty(levelOneValue)){
                            msg.add("第"+rowNum+"行一级分类为空");
                            continue;
                        }
                    }
                    EduSubject subject = this.getByTitle(levelOneValue);
                    EduSubject subjectLevelOne = null;
                    String parentId = null;
                    if(subject == null){//创建一级分类
                        subjectLevelOne = new EduSubject();
                        subjectLevelOne.setTitle(levelOneValue);
                        subjectLevelOne.setSort(0);
                        baseMapper.insert(subjectLevelOne);
                        parentId=subjectLevelOne.getId();
                    }else{
                        parentId = subject.getId();
                    }
                    //二级分类名称
                    String levelTwoValue = "";
                    Cell levelTwoCell = rowData.getCell(1);
                    if(levelTwoCell != null){
                        levelTwoValue = excelHSSFUtil.getCellValue(levelTwoCell);
                        if(StringUtils.isEmpty(levelTwoValue)){
                            msg.add("第"+rowNum+"行二级分类为空");
                            continue;
                        }
                    }
                    EduSubject subjectSub = this.getSubByTitle(levelTwoValue,parentId);
                    EduSubject subjectLevelTwo = null;
                    if(subjectSub == null){//创建二级分类
                        subjectLevelTwo = new EduSubject();
                        subjectLevelTwo.setTitle(levelTwoValue);
                        subjectLevelTwo.setParentId(parentId);
                        subjectLevelTwo.setSort(0);
                        baseMapper.insert(subjectLevelTwo);
                    }

                }
            }
        } catch (IOException e) {
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }

        return msg;
    }

    @Override
    public List<SubjectNestedVo> nestedList() {
        //最终得到的数据列表
        ArrayList<SubjectNestedVo> subjectNestedVoArrayList = new ArrayList<>();
        //获取一级分类数据记录
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",0);
        queryWrapper.orderByAsc("sort","id");
        List<EduSubject> subjects = baseMapper.selectList(queryWrapper);
        //获取二级分类数据记录
        QueryWrapper<EduSubject> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.ne("parent_id",0);
        queryWrapper2.orderByAsc("sort","id");
        List<EduSubject> subSubjects = baseMapper.selectList(queryWrapper2);

        //填充一级分类vo数据
        int count = subjects.size();
        for(int i=0; i<count; i++){
            EduSubject subject = subjects.get(i);
            //创建一级类别vo对象
            SubjectNestedVo subjectNestedVo = new SubjectNestedVo();
            BeanUtils.copyProperties(subject, subjectNestedVo);
            subjectNestedVoArrayList.add(subjectNestedVo);

            //填充二级分类vo数据
            ArrayList<SubjectVo> subjectVoArrayList = new ArrayList<>();
            int count2 = subSubjects.size();
            for(int j=0; j<count2; j++){
                EduSubject subSubject = subSubjects.get(j);
                if (subject.getId().equals(subSubject.getParentId())){
                    //创建二级类别vo对象
                    SubjectVo subjectVo = new SubjectVo();
                    BeanUtils.copyProperties(subSubject,subjectVo);
                    subjectVoArrayList.add(subjectVo);
                }
            }
            subjectNestedVo.setChildren(subjectVoArrayList);
//            subjectNestedVoArrayList.add(subjectNestedVo);
        }


        return subjectNestedVoArrayList;
    }

    @Override
    public boolean saveLevelOne(EduSubject subject) {
        EduSubject subjectLevelOne = this.getByTitle(subject.getTitle());
        if(subjectLevelOne == null){
            return super.save(subject);
        }
        return false;
    }

    @Override
    public boolean saveLevelTwo(EduSubject subject) {
        EduSubject subjectLevelTwo = this.getSubByTitle(subject.getTitle(),subject.getParentId());
        if (subjectLevelTwo == null){
            return this.save(subject);
        }else{
            throw new GuliException(2001,"类别已存在");

        }

    }

    /**
     * 根据分类名称查询这个一级分类中否存在
     * @param title
     * @return
     */
    private EduSubject getByTitle(String title){
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        queryWrapper.eq("parent_id","0");
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据分类名称和父id查询这个二级分类中否存在
     * @param title
     * @param parentId
     * @return
     */
    private EduSubject getSubByTitle(String title,String parentId){
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        queryWrapper.eq("parent_id",parentId);
        return baseMapper.selectOne(queryWrapper);
    }
}
