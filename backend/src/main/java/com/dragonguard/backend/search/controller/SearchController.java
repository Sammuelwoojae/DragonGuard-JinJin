package com.dragonguard.backend.search.controller;

import com.dragonguard.backend.result.dto.response.ResultResponse;
import com.dragonguard.backend.search.dto.request.SearchRequest;
import com.dragonguard.backend.search.entity.SearchType;
import com.dragonguard.backend.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * @author 김승진
 * @description 검색에 대한 요청을 처리하는 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<ResultResponse>> getSearchResult(
            @RequestParam(value = "filters", required = false) List<String> filters,
            @RequestParam String name, @RequestParam SearchType type, @RequestParam Integer page) {
        return ResponseEntity.ok(searchService.getSearchResultByClient(new SearchRequest(name, type, page, filters)));
    }
}
