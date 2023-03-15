package com.dragonguard.backend.member.controller;

import com.dragonguard.backend.global.IdResponse;
import com.dragonguard.backend.member.dto.request.MemberRequest;
import com.dragonguard.backend.member.dto.request.WalletRequest;
import com.dragonguard.backend.member.dto.response.MemberRankResponse;
import com.dragonguard.backend.member.dto.response.MemberResponse;
import com.dragonguard.backend.member.entity.Tier;
import com.dragonguard.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author 김승진
 * @description 멤버 관련 요청을 처리하는 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<IdResponse<UUID>> saveMember(@RequestBody MemberRequest memberRequest) {
        return ResponseEntity.ok(memberService.saveMember(memberRequest));
    }

    @PostMapping("/{id}/commits")
    public ResponseEntity<Void> updateCommits(@PathVariable UUID id) {
        memberService.updateCommits(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable UUID id) {
        return ResponseEntity.ok(memberService.getMember(id));
    }

    @GetMapping("/{id}/tier")
    public ResponseEntity<Tier> getTier(@PathVariable UUID id) {
        return ResponseEntity.ok(memberService.getTier(id));
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<MemberRankResponse>> getTier(
            @PageableDefault(sort = "tokens", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(memberService.getMemberRanking(pageable));
    }

    @PostMapping("/wallet-address")
    public ResponseEntity<Void> updateWalletAddress(@RequestBody WalletRequest walletRequest) {
        memberService.updateWalletAddress(walletRequest);
        return ResponseEntity.ok().build();
    }
}
