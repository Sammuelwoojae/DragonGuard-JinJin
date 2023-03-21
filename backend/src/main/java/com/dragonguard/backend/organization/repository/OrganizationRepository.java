package com.dragonguard.backend.organization.repository;

import com.dragonguard.backend.organization.dto.response.OrganizationResponse;
import com.dragonguard.backend.organization.entity.Organization;
import com.dragonguard.backend.organization.entity.OrganizationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 김승진
 * @description 조직(회사, 대학교) DB CRUD를 수행하는 클래스
 */

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>, OrganizationQueryRepository {
    List<Organization> findByType(OrganizationType organizationType);
}
