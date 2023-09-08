package com.phincon.laza.model.dto.rajaongkir;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllProvinceResponse {
    private Object query;
    private StatusResponse status;
    private List<ProvinceResponse> results;
}
