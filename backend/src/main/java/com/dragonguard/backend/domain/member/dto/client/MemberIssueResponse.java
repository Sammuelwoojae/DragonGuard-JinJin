package com.dragonguard.backend.domain.member.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 김승진
 * @description WebClient 응답에서 이슈 개수를 가져오는 dto
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberIssueResponse {
    private Integer total_count;
}
