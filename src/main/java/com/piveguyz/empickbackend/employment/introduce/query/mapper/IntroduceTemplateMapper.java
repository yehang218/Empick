package com.piveguyz.empickbackend.employment.introduce.query.mapper;

import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceTemplateQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IntroduceTemplateMapper {
    List<IntroduceTemplateQueryDTO> findAllIntroduceTemplate();

    List<IntroduceTemplateQueryDTO> findAllIntroduceTemplateItem();

    IntroduceTemplateQueryDTO findIntroduceTemplateById(int id);
}
