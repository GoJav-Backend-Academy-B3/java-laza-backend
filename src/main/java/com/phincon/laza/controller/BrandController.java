package com.phincon.laza.controller;

import com.phincon.laza.model.dto.request.BrandRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.response.PaginationMeta;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/management/brands")
    public ResponseEntity<DataResponse<Brand>> add(
            @RequestParam("logo_url") MultipartFile logoUrl,
            @RequestParam("name") String name) throws Exception {

        BrandRequest request = new BrandRequest();
        request.setName(name);
        request.setLogoUrl(logoUrl);

        Brand brand = brandService.add(request);


        DataResponse<Brand> dataResponse = new DataResponse<>(
                HttpStatus.CREATED.value(),
                "Success",
                brand,
                null);

        return ResponseEntity.status(HttpStatus.CREATED).body(dataResponse);
    }

    @GetMapping("/brands/{id}")
    public ResponseEntity<DataResponse<Brand>> getById(@PathVariable Long id) {
        Brand brand = brandService.findById(id);

        DataResponse<Brand> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                brand,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @GetMapping("/brands")
    public ResponseEntity<DataResponse<List<Brand>>> getAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Brand> brandPage = brandService.findAll(pageable);
        PaginationMeta paginationMeta = new PaginationMeta(brandPage.getNumber(), brandPage.getSize(), brandPage.getTotalPages());

        List<Brand> brands = brandPage.getContent();
        DataResponse<List<Brand>> dataResponse = new DataResponse<>(HttpStatus.OK.value(), "Success", brands, paginationMeta);
        return ResponseEntity.status(dataResponse.getStatusCode()).body(dataResponse);
    }

    @GetMapping("/brands/search")
    public ResponseEntity<DataResponse<Brand>> getByName(@RequestParam("name") String name) {
        Brand brand = brandService.findByName(name);

        DataResponse<Brand> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                brand,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @PutMapping("/management/brands/{id}")
    public ResponseEntity<DataResponse<Brand>> update(
            @PathVariable Long id,
            @RequestParam("logo_url") MultipartFile logoUrl,
            @RequestParam("name") String name) throws Exception {

        BrandRequest request = new BrandRequest();
        request.setName(name);
        request.setLogoUrl(logoUrl);

        Brand brand = brandService.update(id, request);


        DataResponse<Brand> dataResponse = new DataResponse<>(
                HttpStatus.OK.value(),
                "Success",
                brand,
                null);

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @DeleteMapping("/management/brands/{id}")
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
