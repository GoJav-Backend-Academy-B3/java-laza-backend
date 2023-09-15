package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.SizeRequest;
import com.phincon.laza.model.entity.Size;

import java.util.List;

public interface SizeService {

    List<Size> getAllSize();
    Size getSizeById(Long id) throws Exception;
    Size save( SizeRequest size) throws Exception;
    Size update(Long id, SizeRequest size) throws  Exception;

    void delete(Long id) throws Exception;
}
