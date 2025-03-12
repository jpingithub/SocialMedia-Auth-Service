package com.rb.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidTokenResponse {
    private Boolean isValid;
    private String userName;
}
