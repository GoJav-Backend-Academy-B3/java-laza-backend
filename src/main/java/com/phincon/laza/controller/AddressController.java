package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.security.userdetails.CurrentUser;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<?> add(@CurrentUser SysUserDetails ctx, @Valid @RequestBody AddressRequest request) throws Exception {
        Address address = addressService.add(ctx.getId(), request);

        DataResponse<Address> dataResponse = new DataResponse<>(
                HttpStatus.CREATED.value(),
                "Success",
                address,
                null);

        return ResponseEntity.status(HttpStatus.CREATED).body(dataResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@CurrentUser SysUserDetails ctx) {
        List<Address> addresses = addressService.findAllByUserId(ctx.getId());

        DataResponse<List<Address>> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                addresses,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@CurrentUser SysUserDetails ctx, @PathVariable Long id) throws Exception {
        Address address = addressService.findByIdAndByUserId(ctx.getId(), id);

        DataResponse<Address> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                address,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@CurrentUser SysUserDetails ctx, @Valid @RequestBody AddressRequest request, @PathVariable Long id) throws Exception {
        Address address = addressService.update(ctx.getId(), id, request);

        DataResponse<Address> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                address,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@CurrentUser SysUserDetails ctx, @PathVariable Long id) throws Exception {
        addressService.delete(ctx.getId(), id);

        DataResponse<Address> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                null,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

}
