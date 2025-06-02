package com.piveguyz.empickbackend.mail.query.mapper;

import com.piveguyz.empickbackend.mail.query.dto.MailQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MailMapper {
    List<MailQueryDTO> findAll();

    MailQueryDTO findById();

    List<MailQueryDTO> findByEmail();
}
