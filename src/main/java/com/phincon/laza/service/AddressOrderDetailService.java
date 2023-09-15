package com.phincon.laza.service;

import com.phincon.laza.model.entity.AddressOrderDetail;

import java.util.List;

public interface AddressOrderDetailService {
    List<AddressOrderDetail> getAllAddressOrderDetails();

    AddressOrderDetail getAddressOrderDetailById(String id);

    AddressOrderDetail createAddressOrderDetail(AddressOrderDetail addressOrderDetail);

    AddressOrderDetail updateAddressOrderDetail(String id, AddressOrderDetail addressOrderDetail);

    void deleteAddressOrderDetail(String id);
}

