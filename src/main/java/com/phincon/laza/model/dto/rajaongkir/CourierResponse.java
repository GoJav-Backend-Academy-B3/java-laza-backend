package com.phincon.laza.model.dto.rajaongkir;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CourierResponse
{
    private String code;
    private String name;
    private List<CostsResponse> costs;
}
