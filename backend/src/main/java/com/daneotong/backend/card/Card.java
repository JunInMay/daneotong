package com.daneotong.backend.card;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


import java.time.LocalDateTime;

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
public class Card {

    @Id
    @GeneratedValue
    private Long id;

    private String expression;
    private String meaning;

    private Long userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
