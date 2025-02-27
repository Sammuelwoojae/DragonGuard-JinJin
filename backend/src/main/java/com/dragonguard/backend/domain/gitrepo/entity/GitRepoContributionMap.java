package com.dragonguard.backend.domain.gitrepo.entity;

import com.dragonguard.backend.domain.gitrepomember.dto.client.GitRepoMemberClientResponse;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * @author 김승진
 * @description 깃허브 Repository 기여자들의 기여 정보를 담는 일급 컬렉션
 */

@RequiredArgsConstructor
public class GitRepoContributionMap {
    private final Map<GitRepoMemberClientResponse, Integer> contributions;

    public Integer getContributionByKey(GitRepoMemberClientResponse key) {
        return contributions.get(key);
    }
}
