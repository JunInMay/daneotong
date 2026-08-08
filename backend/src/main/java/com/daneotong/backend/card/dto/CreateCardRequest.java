package com.daneotong.backend.card.dto;

import com.daneotong.backend.card.Card;

public record CreateCardRequest(String expression, String meaning) {

    public Card toEntity() {
        return Card.builder()
                .expression(expression())
                .meaning(meaning())
                .build();
    }
}
