package com.phincon.laza.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ROAllProvinceResponse {
    private Object query;
    private ROStatusResponse status;
    private Optional results;
}
