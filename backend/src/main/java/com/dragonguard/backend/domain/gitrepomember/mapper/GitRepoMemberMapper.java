package com.dragonguard.backend.domain.gitrepomember.mapper;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import com.dragonguard.backend.domain.gitrepomember.dto.response.GitRepoMemberResponse;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoContribution;
import com.dragonguard.backend.domain.gitrepomember.entity.GitRepoMember;
import com.dragonguard.backend.domain.member.entity.Member;
import org.springframework.stereotype.Component;

/**
 * @author 김승진
 * @description GitRepoMember Entity 와 dto 사이의 변환을 도와주는 클래스
 */

@Component
public class GitRepoMemberMapper {

    public GitRepoMember toEntity(final GitRepoMemberResponse gitRepoMemberResponse, final Member member, final GitRepo gitRepo) {
        return GitRepoMember.builder()
                .gitRepoContribution(new GitRepoContribution(gitRepoMemberResponse.getCommits(), gitRepoMemberResponse.getAdditions(), gitRepoMemberResponse.getDeletions()))
                .gitRepo(gitRepo)
                .member(member)
                .build();
    }

    public GitRepoMember toEntity(final Member member, final GitRepo gitRepo) {
        return GitRepoMember.builder()
                .gitRepo(gitRepo)
                .member(member)
                .build();
    }

    public GitRepoMemberResponse toResponse(final GitRepoMember gitRepoMember) {
        GitRepoContribution gitRepoContribution = gitRepoMember.getGitRepoContribution();
        return GitRepoMemberResponse.builder()
                .githubId(gitRepoMember.getMember().getGithubId())
                .additions(gitRepoContribution.getAdditions())
                .deletions(gitRepoContribution.getDeletions())
                .commits(gitRepoContribution.getCommits())
                .build();
    }
}
