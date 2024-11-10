package org.example.market.dto.common;

import lombok.Getter;
import org.example.market.exception.ErrorCode;

@Getter
public class ErrorResponse extends ApiResponse {

    private final ErrorCode errorCode;
    private final String errorMessage;

    public ErrorResponse(ErrorCode errorCode, String errorMessage) {
        super(false);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
