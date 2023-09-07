package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.BrandRequest;
import com.phincon.laza.model.dto.response.BrandResponse;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.PaginationMeta;
import com.phincon.laza.model.dto.response.UserResponse;
import com.phincon.laza.model.entity.Address;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping
    public ResponseEntity<DataResponse<BrandResponse>> add(
            @RequestParam("logo_url") MultipartFile logoUrl,
            @RequestParam("name") String name) throws Exception {

        BrandRequest request = new BrandRequest();
        request.setName(name);
        request.setLogoUrl(logoUrl);

        Brand brand = brandService.add(request);
        BrandResponse brandResponse = new BrandResponse(brand);

        DataResponse<BrandResponse> dataResponse = new DataResponse<>(
                HttpStatus.CREATED.value(),
                "Success",
                brandResponse,
                null);

        return ResponseEntity.status(HttpStatus.CREATED).body(dataResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<BrandResponse>> getById(@PathVariable Long id) {
        Brand brand = brandService.findById(id);
        BrandResponse brandResponse = new BrandResponse(brand);

        DataResponse<BrandResponse> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                brandResponse,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<BrandResponse>>> getAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Brand> brandPage = brandService.findAll(pageable);
        PaginationMeta paginationMeta = new PaginationMeta(brandPage.getNumber(), brandPage.getSize(), brandPage.getTotalPages());

        List<BrandResponse> result = brandPage.getContent().stream().map(BrandResponse::new).collect(Collectors.toList());

       /* List<Brand> brands = brandPage.getContent();*/
        DataResponse<List<BrandResponse>> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Success", result, paginationMeta);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<DataResponse<BrandResponse>> getByName(@RequestParam("name") String name) {
        Brand brand = brandService.findByName(name);
        BrandResponse brandResponse = new BrandResponse(brand);


        DataResponse<BrandResponse> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                brandResponse,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<BrandResponse>> update(
            @PathVariable Long id,
            @RequestParam("logo_url") MultipartFile logoUrl,
            @RequestParam("name") String name) throws Exception {

        BrandRequest request = new BrandRequest();
        request.setName(name);
        request.setLogoUrl(logoUrl);

        Brand brand = brandService.update(id, request);
        BrandResponse brandResponse = new BrandResponse(brand);

        DataResponse<BrandResponse> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                brandResponse,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<?>> delete(@PathVariable Long id) {
        brandService.delete(id);

        DataResponse<Brand> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                null,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

}
