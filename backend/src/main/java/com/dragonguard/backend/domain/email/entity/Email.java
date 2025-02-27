package com.dragonguard.backend.domain.email.entity;

import com.dragonguard.backend.global.audit.AuditListener;
import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.audit.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email implements Auditable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private UUID memberId;

    @Column(nullable = false)
    private Integer code;

    @Setter
    @Embedded
    @Column(nullable = false)
    private BaseTime baseTime;

    @Builder
    public Email(UUID memberId, Integer code) {
        this.memberId = memberId;
        this.code = code;
    }
}
