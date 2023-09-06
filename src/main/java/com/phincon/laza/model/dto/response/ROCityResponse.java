package com.phincon.laza.model.dto.response;

import lombok.Data;

@Data
public class ROCityResponse {
    private String city_id;
    private String province_id;
    private String province;
    private String type;
    private String city_name;
    private String postal_code;
}
