package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceRatingResultCommandDTO;

public interface IntroduceRatingResultCommandService {
    IntroduceRatingResultCommandDTO create(IntroduceRatingResultCommandDTO dto);

    IntroduceRatingResultCommandDTO update(IntroduceRatingResultCommandDTO dto);

    Integer delete(int id);
}
