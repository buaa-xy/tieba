package com.buaa.markpp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The common response model for all apis, including "success", "message" and "data".
 * @param <D> The real data type to be returned
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<D> {
    private boolean success = true;

    private String message;

    private D data;

    public Result<D> withFailure(ExceptionType type) {
        this.success = false;
        this.message = type.getMessage();
        return this;
    }

    public Result<D> withFailure(String message) {
        this.success = false;
        this.message = message;
        return this;
    }

    public Result<D> withData(D data) {
        this.data = data;
        return this;
    }
}
