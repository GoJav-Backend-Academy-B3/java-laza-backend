package com.phincon.laza.model.dto.rajaongkir;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierResponse
{
    private String code;
    private String name;
    private List<CostsResponse> costs;
}
