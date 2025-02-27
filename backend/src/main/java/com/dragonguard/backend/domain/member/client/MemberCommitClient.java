package com.dragonguard.backend.domain.member.client;

import com.dragonguard.backend.domain.member.dto.client.MemberClientRequest;
import com.dragonguard.backend.domain.member.dto.client.MemberCommitResponse;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

/**
 * @author 김승진
 * @description WebClient로 멤버의 커밋을 가져오는 클라이언트
 */

@Component
@RequiredArgsConstructor
public class MemberCommitClient implements GithubClient<MemberClientRequest, MemberCommitResponse> {
    private final WebClient webClient;

    @Override
    public MemberCommitResponse requestToGithub(MemberClientRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("search/commits?q=author:")
                                .path(request.getGithubId() + "+committer-date:%3E" + request.getYear() + "-01-01")
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(MemberCommitResponse.class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }
}
