package com.dragonguard.backend.domain.gitrepo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.IntSummaryStatistics;

/**
 * @author 김승진
 * @description 깃허브 Repository 관련 통계에 대한 응답 정보를 담는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {
    private IntSummaryStatistics commitStats;
    private IntSummaryStatistics additionStats;
    private IntSummaryStatistics deletionStats;
}
