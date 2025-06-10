package com.piveguyz.empickbackend.employment.mail.query.mapper;

import com.piveguyz.empickbackend.employment.mail.query.dto.InterviewMailQueryDTO;
import com.piveguyz.empickbackend.employment.mail.query.dto.JobTestMailQueryDTO;
import com.piveguyz.empickbackend.employment.mail.query.dto.MailQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MailMapper {
    List<MailQueryDTO> findAll();

    MailQueryDTO findById(Integer id);

    List<MailQueryDTO> findByEmail(String email);

    JobTestMailQueryDTO findForJobTestMail(Integer applicationId);

    InterviewMailQueryDTO findForInterviewMail(Integer applicationId);
}
