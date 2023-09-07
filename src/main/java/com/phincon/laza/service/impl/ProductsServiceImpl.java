package com.phincon.laza.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.model.dto.request.CreateUpdateProductRequest;
import com.phincon.laza.model.entity.Brand;
import com.phincon.laza.model.entity.Category;
import com.phincon.laza.model.entity.Product;
import com.phincon.laza.model.entity.Size;
import com.phincon.laza.repository.ProductsRepository;
import com.phincon.laza.service.BrandService;
import com.phincon.laza.service.CategoryService;
import com.phincon.laza.service.CloudinaryImageService;
import com.phincon.laza.service.ProductsService;
import com.phincon.laza.service.SizeService;
import com.phincon.laza.utils.GenerateRandom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {
    private final ProductsRepository productsRepository;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final SizeService sizeService;
    private final CloudinaryImageService cloudinaryImageService;

    public Product getProductById(Long id) throws Exception {
        Optional<Product> productOptional = productsRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new NotFoundException("Product not found");
        }
        return productOptional.get();
    }

    @Override
    public Product create(CreateUpdateProductRequest createProductRequest) throws Exception {
        var product = new Product();
        // fill some fillable field
        product.setName(createProductRequest.name());
        product.setPrice(createProductRequest.price());
        product.setDescription(createProductRequest.description());

        // check for brand
        var brandCompletable = findBrandById(createProductRequest.brandId())
                .thenAcceptAsync(product::setBrand);
        // check for category
        var categoryCompletable = findCategoryById(createProductRequest.categoryId())
                .thenAcceptAsync(product::setCategory);
        // check for sizes
        var sizesCompletable = findSizesByIds(createProductRequest.sizeIds())
                .thenAcceptAsync(product::setSizes);

        CompletableFuture.allOf(brandCompletable, categoryCompletable, sizesCompletable).join();

        var result = cloudinaryImageService.upload(createProductRequest.file().getBytes(), "products",
                GenerateRandom.token());
        product.setImageUrl(result.secureUrl());

        return productsRepository.save(product);
    }

    @Override
    public Product update(Long id, CreateUpdateProductRequest updateProductRequest) throws Exception {
        var product = this.getProductById(id);
        product.setName(updateProductRequest.name());
        product.setPrice(updateProductRequest.price());
        product.setDescription(updateProductRequest.description());

        // check for brand
        var brandCompletable = findBrandById(updateProductRequest.brandId())
                .thenAcceptAsync(product::setBrand);
        // check for category
        var categoryCompletable = findCategoryById(updateProductRequest.categoryId())
                .thenAcceptAsync(product::setCategory);
        // check for sizes
        var sizesCompletable = findSizesByIds(updateProductRequest.sizeIds())
                .thenAcceptAsync(product::setSizes);

        CompletableFuture.allOf(brandCompletable, categoryCompletable, sizesCompletable).join();

        var result = cloudinaryImageService.upload(updateProductRequest.file().getBytes(), "products",
                GenerateRandom.token());
        product.setImageUrl(result.secureUrl());

        return productsRepository.save(product);
    }

    private CompletableFuture<Brand> findBrandById(Long id) throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            return brandService.findById(id);
        });
    }

    private CompletableFuture<Category> findCategoryById(Long id) throws NotFoundException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return categoryService.getCategoryById(id);
            } catch (Exception e) {
                e.printStackTrace();
                throw new NotFoundException("Category not found");
            }
        });
    }

    private CompletableFuture<List<Size>> findSizesByIds(List<Long> ids) throws NotFoundException {
        return CompletableFuture.supplyAsync(() -> {
            return ids.stream().map(t -> {
                try {
                    return sizeService.getSizeById(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new NotFoundException("size not found");
                }
            }).collect(Collectors.toList());
        });
    }

}
