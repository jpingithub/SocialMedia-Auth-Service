package com.rb.auth.dto;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String message;
    private String timeStamp;
    private String path;
}
