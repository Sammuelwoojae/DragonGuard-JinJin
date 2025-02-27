package com.dragonguard.backend.domain.gitrepo.mapper;

import com.dragonguard.backend.domain.gitrepo.entity.GitRepo;
import org.mapstruct.Mapper;

/**
 * @author 김승진
 * @description 깃허브 레포지토리 Entity와 dto의 변환을 돕는 클래스
 */

@Mapper(componentModel = "spring")
public interface GitRepoMapper {
    GitRepo toEntity(final String name);
}
