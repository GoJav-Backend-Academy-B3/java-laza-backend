package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.SizeRequest;
import com.phincon.laza.model.entity.Size;

import java.util.List;

public interface SizeService {

    List<Size> getAllSize();
    Size getSizeById(Long id);
    Size save( SizeRequest size);
    Size update(Long id, SizeRequest size);

    void delete(Long id);
}
