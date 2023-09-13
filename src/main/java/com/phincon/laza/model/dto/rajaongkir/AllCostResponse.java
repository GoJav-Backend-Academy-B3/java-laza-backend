package com.phincon.laza.model.dto.rajaongkir;

import com.phincon.laza.model.dto.request.ROCostRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllCostResponse {
    private StatusResponse status;
    private ROCostRequest query;
    private CityResponse origin_details;
    private CityResponse destination_details;
    private List<CourierResponse> results;
}

