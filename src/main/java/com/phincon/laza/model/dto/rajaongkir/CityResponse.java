package com.phincon.laza.model.dto.rajaongkir;

import lombok.Data;

@Data
public class CityResponse {
    private String city_id;
    private String province_id;
    private String province;
    private String type;
    private String city_name;
    private String postal_code;
}
