package com.phincon.laza.model.dto.rajaongkir;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CostsResponse {
    private String service;
    private String description;
    private List<CostResponse> cost;
}
