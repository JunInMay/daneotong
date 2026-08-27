package com.daneotong.backend.card;

import com.daneotong.backend.card.dto.CreateCardRequest;
import com.daneotong.backend.card.dto.CreateCardResponse;
import com.daneotong.backend.common.ApiResponse;
import com.daneotong.backend.common.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping()
    public ApiResponse<CreateCardResponse> createCard(
            @RequestBody CreateCardRequest request) {

        Cards cards = request.toEntity();

        return ApiResponse.of(ResponseCode.SUCCESS
                , cardService.createCard(cards));
    }
}
