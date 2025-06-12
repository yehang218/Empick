package com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.applicationBookmark;

import lombok.*;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantBookmarkId implements Serializable {
    private int memberId;
    private int applicantId;
}
