package com.piveguyz.empickbackend.mail.command.domain.repository;

import com.piveguyz.empickbackend.interview.command.domain.aggregate.InterviewEntity;
import com.piveguyz.empickbackend.mail.command.domain.aggregate.MailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<MailEntity, Integer> {
}
