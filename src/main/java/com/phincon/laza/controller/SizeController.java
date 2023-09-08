package com.phincon.laza.controller;


import com.phincon.laza.model.dto.request.SizeRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.SizeResponse;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Size;
import com.phincon.laza.service.SizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/size")
@RequiredArgsConstructor
public class SizeController {
    private final SizeService sizeService;

    @GetMapping
    public ResponseEntity<DataResponse<List<SizeResponse>>> getAllSizes() {
        List<Size> sizes = sizeService.getAllSize();
        List<SizeResponse> sizeResponses = sizes.stream().map(SizeResponse::new).collect(Collectors.toList());
        DataResponse<List<SizeResponse>> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Success", sizeResponses, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<SizeResponse>> getSizeById(@PathVariable Long id) throws Exception {
        Size size = sizeService.getSizeById(id);
        SizeResponse result = new SizeResponse(size);
        DataResponse<SizeResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Success", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<DataResponse<SizeResponse>> createSize(@Valid @RequestBody SizeRequest request) throws Exception {
        Size size = sizeService.save(request);
        SizeResponse result = new SizeResponse(size);
        DataResponse<SizeResponse> dataResponse = new DataResponse<>(HttpStatus.CREATED.value(), "Size created successfully", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<SizeResponse>> updateSize(@PathVariable Long id, @Valid @RequestBody SizeRequest request) throws Exception {
        Size size = sizeService.update(id, request);
        SizeResponse result = new SizeResponse(size);
        DataResponse<SizeResponse> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Size updated successfully", result, null);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<Void>> deleteSize(@PathVariable Long id) throws Exception {
        sizeService.delete(id);
        DataResponse<Void> dataResponse = new DataResponse<>();
        dataResponse.setStatusCode(HttpStatus.OK.value());
        dataResponse.setMessage("Size deleted successfully");
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }
}

