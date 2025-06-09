package com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.applicant.command.application.dto.ApplicantCommandDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applicant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(name = "profile_url", nullable = false)
    private String profileUrl;

    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    private String address;

    public void updateFromEntity(ApplicantEntity updated) {
        this.name = updated.getName();
        this.phone = updated.getPhone();
        this.email = updated.getEmail();
        this.profileUrl = updated.getProfileUrl();
        this.birth = updated.getBirth();
        this.address = updated.getAddress();
    }
}
