package com.dragonguard.backend.domain.commit.entity;

import com.dragonguard.backend.domain.member.entity.Member;
import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;

import javax.persistence.*;

/**
 * @author 김승진
 * @description 커밋 정보를 담는 DB Entity
 */

@Getter
@Entity
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Commit implements Auditable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer amount;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Commit(Integer year, Integer amount, Member member) {
        this.year = year;
        this.amount = amount;
        this.member = member;
        organize();
    }

    public boolean customEqualsWithAmount(Commit commit) {
        return year.equals(commit.year) && amount.equals(commit.amount) && member.getGithubId().equals(commit.member.getGithubId());
    }

    public boolean customEquals(Commit commit) {
        return year.equals(commit.year) && member.getGithubId().equals(commit.member.getGithubId());
    }

    private void organize() {
        member.addCommit(this);
    }
}
