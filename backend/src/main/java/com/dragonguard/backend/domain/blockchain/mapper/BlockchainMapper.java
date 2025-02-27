package com.dragonguard.backend.domain.blockchain.mapper;

import com.dragonguard.backend.domain.blockchain.dto.request.ContractRequest;
import com.dragonguard.backend.domain.blockchain.dto.response.BlockchainResponse;
import com.dragonguard.backend.domain.blockchain.entity.Blockchain;
import com.dragonguard.backend.domain.blockchain.entity.ContributeType;
import com.dragonguard.backend.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigInteger;

/**
 * @author 김승진
 * @description 블록체인 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring", imports = {ContributeType.class})
public interface BlockchainMapper {
    @Mapping(target = "contributeType", expression = "java(ContributeType.valueOf(request.getContributeType()))")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "transactionHash", source = "transactionHash")
    Blockchain toEntity(final BigInteger amount, final Member member, final ContractRequest request, final String transactionHash);
    @Mapping(target = "memberId", source = "blockchain.member.id")
    @Mapping(target = "githubId", source = "blockchain.member.githubId")
    @Mapping(target = "createdAt", source = "blockchain.baseTime.createdAt")
    @Mapping(target = "transactionHashUrl", expression = "java(blockchain.getTransactionHashUrl())")
    BlockchainResponse toResponse(final Blockchain blockchain);
}
