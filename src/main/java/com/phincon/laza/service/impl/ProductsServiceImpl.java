package com.phincon.laza.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductsServiceImpl implements ProductsService {
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SizeService sizeService;
    @Autowired
    private CloudinaryImageService cloudinaryImageService;

    @Autowired
    private Executor asyncExecutor;

    @Override
    public Page<Product> getAll(int page, int size) {
        return productsRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        Optional<Product> productOptional = productsRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new NotFoundException("Product not found");
        }
        return productOptional.get();
    }

    @Override
    public Page<Product> findProductByName(String keyword, int page, int size) {
        return productsRepository.findByNameContaining(keyword, PageRequest.of(page, size));
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

        try {
            CompletableFuture.allOf(brandCompletable, categoryCompletable, sizesCompletable).join();
        } catch (CompletionException e) {
            throw (NotFoundException) e.getCause();
        }

        var result = cloudinaryImageService.upload(createProductRequest.imageFile().getBytes(), "products",
                GenerateRandom.token());
        product.setImageUrl(result.secureUrl());
        product.setCloudinaryPublicId(result.publicId());

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

        cloudinaryImageService.delete(product.getCloudinaryPublicId());
        var result = cloudinaryImageService.upload(updateProductRequest.imageFile().getBytes(), "products",
                GenerateRandom.token());
        product.setImageUrl(result.secureUrl());

        return productsRepository.save(product);
    }

    @Override
    public void delete(Long id) throws Exception {
        var product = this.getProductById(id);
        String publicId = product.getCloudinaryPublicId();

        cloudinaryImageService.delete(publicId);

        productsRepository.delete(product);
    }

    private CompletableFuture<Brand> findBrandById(Long id) throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            log.info("findBrandById({})", id);
            return brandService.findById(id);
        }, asyncExecutor);
    }

    private CompletableFuture<Category> findCategoryById(Long id) throws NotFoundException {
        return CompletableFuture.supplyAsync(() -> {
            log.info("findCategoryById({})", id);
            try {
                return categoryService.getCategoryById(id);
            } catch (Exception e) {
                e.printStackTrace();
                throw new NotFoundException("Category not found");
            }
        }, asyncExecutor);
    }

    private CompletableFuture<List<Size>> findSizesByIds(List<Long> ids) throws NotFoundException {
        log.info("findSizeByIds({})", ids);
        return CompletableFuture.supplyAsync(() -> {
            return ids.parallelStream().map((Long id) -> {
                try {
                    log.info("inside findSizeByIds, getSizeById({})", id);
                    return sizeService.getSizeById(id);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new NotFoundException("size not found");
                }
            }).collect(Collectors.toList());
        }, asyncExecutor);
    }

}
