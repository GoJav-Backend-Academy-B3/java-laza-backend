package com.phincon.laza.model.dto.rajaongkir;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CostResponse {
    private Integer value;
    private String etd;
    private String note;
}
