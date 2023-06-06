package com.dragonguard.backend.domain.gitorganization.entity;

import com.dragonguard.backend.global.audit.BaseTime;
import com.dragonguard.backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 깃허브 Organization 과 Member 사이의 M 대 N 관계 중간 엔티티
 */

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitOrganizationMember extends BaseTime {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn
    private GitOrganization gitOrganization;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn
    private Member member;

    @Builder
    public GitOrganizationMember(GitOrganization gitOrganization, Member member) {
        this.gitOrganization = gitOrganization;
        this.member = member;
        organize();
    }

    private void organize() {
        this.member.organizeGitOrganizationMember(this);
    }
}
