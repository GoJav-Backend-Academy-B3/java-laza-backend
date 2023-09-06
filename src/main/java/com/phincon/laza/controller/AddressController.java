package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.AddressRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<?> add(@AuthenticationPrincipal UserDetails ctx, @Valid @RequestBody AddressRequest request) throws Exception {
        Address address = addressService.add(ctx.getUsername(), request);

        DataResponse<Address> dataResponse = new DataResponse<>(
                HttpStatus.CREATED.value(),
                "Success",
                address,
                null);

        return ResponseEntity.status(HttpStatus.CREATED).body(dataResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@AuthenticationPrincipal UserDetails ctx) {
        List<Address> addresses = addressService.findAllByUsername(ctx.getUsername());

        DataResponse<List<Address>> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                addresses,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws Exception {
        Address address = addressService.findById(id);

        DataResponse<Address> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                address,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserDetails ctx, @Valid @RequestBody AddressRequest request, @PathVariable Long id) throws Exception {
        Address address = addressService.update(ctx.getUsername(), id, request);

        DataResponse<Address> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                address,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        addressService.delete(id);

        DataResponse<Address> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                null,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

}
