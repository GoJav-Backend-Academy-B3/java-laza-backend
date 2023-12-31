package com.phincon.laza.model.dto.response;

import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class SizeResponse {
    private Long id;
    private String size;

    public SizeResponse(Size size) {
        this.id = size.getId();
        this.size = size.getSize();
    }

}
