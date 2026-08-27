package com.daneotong.backend.card.dto;

import com.daneotong.backend.card.Cards;

public record CreateCardRequest(
        String expression
        , String meaning) {

    public Cards toEntity() {
        return Cards.builder()
                .expression(expression())
                .meaning(meaning())
                .build();
    }
}
