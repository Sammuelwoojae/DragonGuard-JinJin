package com.dragonguard.backend.organization.repository;

import com.dragonguard.backend.member.entity.QMember;
import com.dragonguard.backend.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.organization.entity.QOrganization;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dragonguard.backend.member.entity.QMember.member;
import static com.dragonguard.backend.organization.entity.QOrganization.organization;

/**
 * @author 김승진
 * @description Organization DB 조회 접근에 대한 구현체
 */

@Repository
@RequiredArgsConstructor
public class OrganizationQueryRepositoryImpl implements OrganizationQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final OrganizationQDtoFactory organizationQDtoFactory;

    @Override
    public List<OrganizationResponse> findRank(Pageable pageable) {
        return jpaQueryFactory
                .select(organizationQDtoFactory.qOrganizationResponse())
                .from(organization, member)
                .leftJoin(organization.members, member)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.sumOfTokens.sum().desc())
                .fetch();
    }
}
