package com.piveguyz.empickbackend.mailTemplate.query.mapper;

import com.piveguyz.empickbackend.mailTemplate.query.dto.MailTemplateQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MailTemplateMapper {
    List<MailTemplateQueryDTO> findAll();

    List<MailTemplateQueryDTO> findByTitle(String title);
}
