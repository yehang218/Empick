package com.pive.empickbackend.member.command.domain.repository;

import com.pive.empickbackend.member.command.domain.aggregate.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByEmployeeNumber(int empNum);
}
