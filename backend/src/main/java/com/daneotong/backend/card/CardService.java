package com.daneotong.backend.card;

import com.daneotong.backend.card.dto.CreateCardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public CreateCardResponse createCard(Cards cards) {
        Cards saved = cardRepository.save(cards);
        return CreateCardResponse.from(saved);
    }
}
