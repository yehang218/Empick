package com.piveguyz.empickbackend.orgstructure.member.command.domain.repository;

import com.piveguyz.empickbackend.orgstructure.member.command.domain.aggregate.MemberEditProposalEntity;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.enums.MemberEditStatus;
import com.piveguyz.empickbackend.orgstructure.member.command.domain.enums.MemberTargetField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEditProposalRepository extends JpaRepository<MemberEditProposalEntity, Integer> {
    boolean existsByMemberIdAndTargetFieldAndStatus(Integer memberId,
                                                    MemberTargetField targetField,
                                                    MemberEditStatus status);
}
