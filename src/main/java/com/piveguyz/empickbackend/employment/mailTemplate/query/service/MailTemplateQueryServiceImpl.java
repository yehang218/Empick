package com.piveguyz.empickbackend.employment.mailTemplate.query.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.mailTemplate.query.dto.MailTemplateQueryDTO;
import com.piveguyz.empickbackend.employment.mailTemplate.query.mapper.MailTemplateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class  MailTemplateQueryServiceImpl implements MailTemplateQueryService {
    private final MailTemplateMapper mailTemplateMapper;

    @Override
    public List<MailTemplateQueryDTO> findAll() {
        List<MailTemplateQueryDTO> mailTemplateQueryDTOList = mailTemplateMapper.findAll();
        if(mailTemplateQueryDTOList.isEmpty()){
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NOT_FOUND);
        }
        return mailTemplateQueryDTOList;
    }

    @Override
    public MailTemplateQueryDTO findById(Integer id) {
        MailTemplateQueryDTO mailTemplateQueryDTO = mailTemplateMapper.findById(id);
        if(mailTemplateQueryDTO == null) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NOT_FOUND);
        }
        return mailTemplateQueryDTO;
    }

    @Override
    public MailTemplateQueryDTO findByTitle(String title) {
        MailTemplateQueryDTO mailTemplateQueryDTO = mailTemplateMapper.findByTitle(title);
        if(mailTemplateQueryDTO == null){
            throw new BusinessException(ResponseCode.EMPLOYMENT_MAIL_TEMPLATE_NOT_FOUND);
        }
        return mailTemplateQueryDTO;
    }

    // 검색은 결과가 안 나올 수도 있어서 오류 코드를 안 뺐습니다.
    @Override
    public List<MailTemplateQueryDTO> searchByTitle(String title) {
        List<MailTemplateQueryDTO> mailTemplateQueryDTOList = mailTemplateMapper.searchByTitle(title);
        return mailTemplateQueryDTOList;
    }
}
