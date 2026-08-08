package com.daneotong.backend.card;

import com.daneotong.backend.card.dto.CreateCardRequest;
import com.daneotong.backend.card.dto.CreateCardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public CreateCardResponse createCard(Card card) {
        Card saved = cardRepository.save(card);
        return CreateCardResponse.from(saved);
    }
}
