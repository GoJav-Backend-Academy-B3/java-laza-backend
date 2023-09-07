package com.phincon.laza.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ROCostRequest {
    private String origin;
    private String destination;
    private Integer weight;
    private String courier;
}
