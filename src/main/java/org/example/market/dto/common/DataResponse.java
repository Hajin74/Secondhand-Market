package org.example.market.dto.common;

import lombok.Getter;

@Getter
public class DataResponse<T> extends ApiResponse {

    private final T data;

    public DataResponse(T data) {
        super(true);
        this.data = data;
    }

}
