package com.piveguyz.empickbackend.employment.introduce.query.service;

import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceQueryDTO;
import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceTemplateItemResponseQueryDTO;
import com.piveguyz.empickbackend.employment.introduce.query.mapper.IntroduceTemplateItemResponseQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IntroduceTemplateItemResponseQueryServiceImp implements IntroduceTemplateItemResponseQueryService {

    private final IntroduceTemplateItemResponseQueryMapper introduceTemplateItemResponseQueryMapper;

    @Override
    public List<IntroduceTemplateItemResponseQueryDTO> findAllIntroduceTemplateItemResponse() {

        return introduceTemplateItemResponseQueryMapper.findAllIntroduceTemplateItemResponse();
    }
}
