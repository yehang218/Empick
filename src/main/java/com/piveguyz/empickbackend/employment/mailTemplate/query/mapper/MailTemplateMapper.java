package com.piveguyz.empickbackend.employment.mailTemplate.query.mapper;

import com.piveguyz.empickbackend.employment.mailTemplate.query.dto.MailTemplateQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MailTemplateMapper {
    List<MailTemplateQueryDTO> findAll();

    MailTemplateQueryDTO findByTitle(String title);

    List<MailTemplateQueryDTO> searchByTitle(String title);

    MailTemplateQueryDTO findById(Integer id);
}
