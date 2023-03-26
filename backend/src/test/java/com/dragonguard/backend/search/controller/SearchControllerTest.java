package com.dragonguard.backend.search.controller;

import com.dragonguard.backend.commit.entity.Commit;
import com.dragonguard.backend.member.entity.Member;
import com.dragonguard.backend.member.entity.Role;
import com.dragonguard.backend.member.repository.MemberRepository;
import com.dragonguard.backend.member.service.AuthService;
import com.dragonguard.backend.result.dto.response.ResultResponse;
import com.dragonguard.backend.search.service.SearchService;
import com.dragonguard.backend.support.DatabaseTest;
import com.dragonguard.backend.support.docs.RestDocumentTest;
import com.dragonguard.backend.support.fixture.member.entity.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentRequest;
import static com.dragonguard.backend.support.docs.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DatabaseTest
@WebMvcTest(SearchController.class)
class SearchControllerTest extends RestDocumentTest {
    @MockBean
    private SearchService searchService;
    @MockBean
    protected AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    protected Member loginUser;

    @BeforeEach
    public void setup() {
        Member member = MemberFixture.SAMPLE1.toMember();
        loginUser = memberRepository.save(member);
        when(authService.getLoginUser()).thenReturn(loginUser);
    }


    @Test
    @DisplayName("검색 결과 조회")
    void getSearchResult() throws Exception {
        // given
        List<ResultResponse> expected = Arrays.asList(
                new ResultResponse(1L, "ohksj77"),
                new ResultResponse(2L, "HJ39"),
                new ResultResponse(3L, "posite"),
                new ResultResponse(4L, "Sammuelwoojae"),
                new ResultResponse(5L, "And"),
                new ResultResponse(6L, "DragonGuard-JinJin"));

        given(searchService.getSearchResultByClient(any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/search?page=1&name=gitrank&type=REPOSITORIES")
                                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // docs
        perform.andDo(print())
                .andDo(document("get search result", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("검색 결과 필터링 조회")
    void getSearchResultByFiltering() throws Exception {
        // given
        List<ResultResponse> expected = Arrays.asList(
                new ResultResponse(1L, "ohksj77"),
                new ResultResponse(2L, "HJ39"),
                new ResultResponse(3L, "posite"),
                new ResultResponse(4L, "Sammuelwoojae"),
                new ResultResponse(5L, "And"),
                new ResultResponse(6L, "DragonGuard-JinJin"));

        given(searchService.getSearchResultByClient(any())).willReturn(expected);

        // when
        ResultActions perform =
                mockMvc.perform(
                        get("/search?page=1&name=gitrank&type=REPOSITORIES&filters=language:swift,language:kotlin,language:java")
                                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // docs
        perform.andDo(print())
                .andDo(document("get search result by filtering", getDocumentRequest(), getDocumentResponse()));
    }
}
