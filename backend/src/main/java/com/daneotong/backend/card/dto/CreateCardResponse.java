package com.daneotong.backend.card.dto;

import com.daneotong.backend.card.Card;

import java.time.LocalDateTime;

public record CreateCardResponse(Long id, String expression, String meaning, LocalDateTime createdAt) {
    public static CreateCardResponse from(Card card) {
        return new CreateCardResponse(card.getId(), card.getExpression(), card.getMeaning(), card.getCreatedAt());
    }
}
