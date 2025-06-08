package com.piveguyz.empickbackend.employment.introduce.query.service;

import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceTemplateQueryDTO;
import com.piveguyz.empickbackend.employment.introduce.query.mapper.IntroduceTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IntroduceTemplateQueryServiceImp implements IntroduceTemplateQueryService {

    private final IntroduceTemplateMapper introduceTemplateMapper;


@Override
public List<IntroduceTemplateQueryDTO> findAllIntroduceTemplate() {
    return introduceTemplateMapper.findAllIntroduceTemplate();
    }
}