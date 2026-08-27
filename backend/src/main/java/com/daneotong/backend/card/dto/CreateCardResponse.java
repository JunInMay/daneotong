package com.daneotong.backend.card.dto;

import com.daneotong.backend.card.Cards;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateCardResponse(UUID id, String expression, String meaning, LocalDateTime createdAt) {
    public static CreateCardResponse from(Cards cards) {
        return new CreateCardResponse(cards.getId(), cards.getExpression(), cards.getMeaning(), cards.getCreatedAt());
    }
}
