package com.piveguyz.empickbackend.employment.introduce.command.application.service;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceRatingResultCreateCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceRatingResultUpdateCommandDTO;
import jakarta.validation.Valid;

public interface IntroduceRatingResultCommandService {
    IntroduceRatingResultCreateCommandDTO create(IntroduceRatingResultCreateCommandDTO dto);

    Integer delete(int id);

    IntroduceRatingResultUpdateCommandDTO update(int id, @Valid IntroduceRatingResultUpdateCommandDTO dto);
}
