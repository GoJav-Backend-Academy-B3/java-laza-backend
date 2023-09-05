package com.phincon.laza.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationMeta {
    private Integer page;
    private Integer size;
    private Integer count;
}
