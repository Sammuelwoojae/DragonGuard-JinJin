package com.dragonguard.backend.domain.gitrepo.client;

import com.dragonguard.backend.domain.gitrepo.dto.client.GitRepoClientRequest;
import com.dragonguard.backend.global.GithubClient;
import com.dragonguard.backend.global.exception.WebClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author 김승진
 * @description 깃허브 Repository 사용 언어 정보를 Github REST API에 요청하는 클래스
 */

@Component
@RequiredArgsConstructor
public class GitRepoLanguageClient implements GithubClient<GitRepoClientRequest, Map<String, Integer>> {
    private final WebClient webClient;

    @Override
    public Map<String, Integer> requestToGithub(GitRepoClientRequest request) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path("repos/")
                                .path(request.getName())
                                .path("/languages")
                                .build())
                .headers(headers -> headers.setBearerAuth(request.getGithubToken()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(Map.class)
                .blockOptional()
                .orElseThrow(WebClientException::new);
    }
}
