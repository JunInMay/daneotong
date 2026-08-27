package com.daneotong.backend.card;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 단어 카드 엔티티
 * @author joonyi
 * */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cards {

    @Id
    @GeneratedValue
    private UUID id;

    private String expression;
    private String meaning;

    private UUID userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
