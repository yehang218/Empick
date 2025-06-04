package com.piveguyz.empickbackend.employment.mailTemplate.command.domain.repository;

import com.piveguyz.empickbackend.employment.mailTemplate.command.domain.aggregate.MailTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailTemplateRepository extends JpaRepository<MailTemplateEntity, Integer> {
    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Integer id);
}
