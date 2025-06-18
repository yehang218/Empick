package com.piveguyz.empickbackend.employment.introduce.query.mapper;

import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceTemplateItemResponseQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IntroduceTemplateItemResponseQueryMapper {
    List<IntroduceTemplateItemResponseQueryDTO> findAllIntroduceTemplateItemResponse();
}
