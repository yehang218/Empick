package com.piveguyz.empickbackend.employment.interview.query.mapper;

import com.piveguyz.empickbackend.employment.interview.query.dto.InterviewQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface InterviewMapper {
    List<InterviewQueryDTO> findAll();

    InterviewQueryDTO findById(Integer id);

    List<InterviewQueryDTO> findByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<InterviewQueryDTO> checkAvailable(LocalDateTime start, LocalDateTime end);
}
