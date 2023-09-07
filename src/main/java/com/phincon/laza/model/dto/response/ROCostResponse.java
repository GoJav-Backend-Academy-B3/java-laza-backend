package com.phincon.laza.model.dto.response;

import com.phincon.laza.model.dto.request.ROCostRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ROCostResponse {
    private ROStatusResponse status;
    private ROCostRequest query;
    private ROCityResponse origin_details;
    private ROCityResponse destination_details;
    private List<ROCourierResponse> results;
}

