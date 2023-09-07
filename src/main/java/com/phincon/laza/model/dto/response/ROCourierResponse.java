package com.phincon.laza.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ROCourierResponse {
    private String code;
    private String name;
    private Optional costs;
}
