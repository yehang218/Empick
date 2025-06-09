package com.piveguyz.empickbackend.employment.applicationItem.query.service;

import java.util.List;

import com.piveguyz.empickbackend.employment.applicationItem.query.dto.ApplicationItemQueryDTO;

public interface ApplicationItemQueryService {
	List<ApplicationItemQueryDTO> getItemsByRecruitmentId(int recruitmentId);
}
