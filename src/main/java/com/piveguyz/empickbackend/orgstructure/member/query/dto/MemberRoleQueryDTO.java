package com.piveguyz.empickbackend.orgstructure.member.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberRoleQueryDTO {
    String code;
    String name;
    String description;
    Integer roleType;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
}
