package com.piveguyz.empickbackend.orgstructure.rank.command.domain.repository;

import com.piveguyz.empickbackend.orgstructure.rank.command.domain.aggregate.RankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 직급 엔티티 Repository
 */
@Repository
public interface RankRepository extends JpaRepository<RankEntity, Integer> {

    /**
     * 코드로 직급 존재 여부 확인
     * @param code 직급 코드
     * @return 존재 여부
     */
    boolean existsByCode(String code);

    /**
     * 코드로 직급 조회
     * @param code 직급 코드
     * @return 직급 엔티티 (Optional)
     */
    Optional<RankEntity> findByCode(String code);
}