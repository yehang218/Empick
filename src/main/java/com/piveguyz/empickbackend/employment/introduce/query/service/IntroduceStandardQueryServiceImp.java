package com.piveguyz.empickbackend.employment.introduce.query.service;

import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceStandardItemQueryDTO;
import com.piveguyz.empickbackend.employment.introduce.query.dto.IntroduceStandardQueryDTO;
import com.piveguyz.empickbackend.employment.introduce.query.mapper.IntroduceStandardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IntroduceStandardQueryServiceImp implements IntroduceStandardQueryService {

    private final IntroduceStandardMapper IntroduceStandardMapper;

    @Override
    public List<IntroduceStandardQueryDTO> findAllIntroduceStandard() {
        return IntroduceStandardMapper.findAllIntroduceStandard();
    }

    @Override
    public List<IntroduceStandardItemQueryDTO> findAllIntroduceStandardItem() {
        return IntroduceStandardMapper.findAllIntroduceStandardItem();
    }
}
