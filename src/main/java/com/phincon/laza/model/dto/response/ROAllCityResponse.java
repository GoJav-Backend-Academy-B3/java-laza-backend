package com.phincon.laza.model.dto.response;


import lombok.Data;

import java.util.List;

@Data
public class ROAllCityResponse {
    private Object query;
    private ROStatusResponse status;
    private List<ROCityResponse> results;
}
