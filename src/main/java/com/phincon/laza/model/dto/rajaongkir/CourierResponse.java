package com.phincon.laza.model.dto.rajaongkir;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierResponse {
    private String code;
    private String name;
    private Optional costs;
}
