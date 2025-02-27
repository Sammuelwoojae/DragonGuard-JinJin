package com.dragonguard.backend.domain.contribution.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 김승진
 * @description 커밋 관련 응답 정보를 담는 dto
 */

@Getter
@AllArgsConstructor
public class ContributionScrapingResponse {
    private String githubId;
    private Integer commitNum;
}
