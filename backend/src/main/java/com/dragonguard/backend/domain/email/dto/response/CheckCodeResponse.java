package com.dragonguard.backend.domain.email.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckCodeResponse {
    private boolean isValidCode;
}
