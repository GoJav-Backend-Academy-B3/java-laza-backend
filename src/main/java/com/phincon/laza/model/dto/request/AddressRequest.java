package com.phincon.laza.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class AddressRequest {

    @NotBlank(message = "province is required")
    private String province_id;

    @NotBlank(message = "city is required")
    private String city_id;

    @NotBlank(message = "full address is required")
    private String fullAddress;

    @NotBlank(message = "receiver name is required")
    @JsonProperty("receiver_name")
    private String receiverName;

    @NotBlank(message = "phone number is required")
    private String phone;

    @JsonProperty("is_primary")
    private boolean isPrimary;

}
