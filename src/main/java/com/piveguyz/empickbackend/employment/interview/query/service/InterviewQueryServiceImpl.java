package com.piveguyz.empickbackend.employment.interview.query.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.interview.query.dto.InterviewQueryDTO;
import com.piveguyz.empickbackend.employment.interview.query.mapper.InterviewMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterviewQueryServiceImpl implements InterviewQueryService {
    private final InterviewMapper mapper;

    @Override
    public List<InterviewQueryDTO> findAll() {
        List<InterviewQueryDTO> dtoList = mapper.findAll();
        return dtoList;
    }

    @Override
    public InterviewQueryDTO findById(Integer id) {
        InterviewQueryDTO dto = mapper.findById(id);
        if(dto == null){
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND);
        }
        return dto;
    }

    @Override
    public List<InterviewQueryDTO> findByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 23:59:59.999999999

        List<InterviewQueryDTO> dtoList = mapper.findByDate(startOfDay, endOfDay);
        return dtoList;
    }

    @Override
    public Boolean checkAvailable(LocalDateTime datetime) {
        // 입력 시간 기준 ±30분 계산
        LocalDateTime start = datetime.minusMinutes(30);
        LocalDateTime end = datetime.plusMinutes(30);

        // ±30분 범위에 있는 일정들을 조회
        List<InterviewQueryDTO> dtoList = mapper.checkAvailable(start, end);

        // 일정이 없으면 등록 가능 (true), 있으면 불가능 (false)
        return dtoList.isEmpty();
    }
}
