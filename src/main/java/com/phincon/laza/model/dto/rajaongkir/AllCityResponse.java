package com.phincon.laza.model.dto.rajaongkir;


import lombok.Data;

import java.util.Optional;

@Data
public class AllCityResponse {
    private Object query;
    private StatusResponse status;
    private Optional results;
}
