package com.piveguyz.empickbackend.member.command.domain.repository;

import com.piveguyz.empickbackend.member.command.domain.aggregate.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    Optional<MemberEntity> findByEmployeeNumber(int empNum);
    boolean existsByEmail(String email);

    boolean existsByEmployeeNumber(int randomNumber);
}
