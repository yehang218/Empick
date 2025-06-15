package com.piveguyz.empickbackend.orgstructure.rank.command.application.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.orgstructure.rank.command.application.dto.RankCommandDTO;

/**
 * 직급 Command Service Interface
 * 직급 생성, 수정, 삭제 등의 명령 처리를 담당하는 서비스 인터페이스
 */
public interface RankCommandService {

    /**
     * 직급 생성
     *
     * @param rankCommandDTO 생성할 직급 정보
     * @return 생성된 직급 정보를 포함한 API 응답
     */
    CustomApiResponse<RankCommandDTO> createRank(RankCommandDTO rankCommandDTO);

    /**
     * 직급 수정
     *
     * @param id 수정할 직급 ID
     * @param rankCommandDTO 수정할 직급 정보
     * @return 수정된 직급 정보를 포함한 API 응답
     */
    CustomApiResponse<RankCommandDTO> updateRank(Integer id, RankCommandDTO rankCommandDTO);

    /**
     * 직급 삭제
     *
     * @param id 삭제할 직급 ID
     * @return 삭제 완료 API 응답
     */
    CustomApiResponse<Void> deleteRank(Integer id);

    /**
     * 직급 활성 상태 변경
     *
     * @param id 대상 직급 ID
     * @param isActive 변경할 활성 상태
     * @return 수정된 직급 정보를 포함한 API 응답
     */
    CustomApiResponse<RankCommandDTO> updateRankStatus(Integer id, IsActive isActive);
}