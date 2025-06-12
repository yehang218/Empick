package com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.applicationBookmark;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applicant_bookmark")
@Getter
@Setter
@NoArgsConstructor
public class ApplicantBookmarkEntity {

    @EmbeddedId
    private ApplicantBookmarkId id;

    public ApplicantBookmarkEntity(ApplicantBookmarkId id) {
        this.id = id;
    }
}
