package com.phincon.laza.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
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

@Service
@Slf4j
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
    private Cloudinary cloudinary;

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
        product.setName(createProductRequest.getName());
        product.setPrice(createProductRequest.getPrice());
        product.setDescription(createProductRequest.getDescription());

        // check for brand
        var brandCompletable = findBrandById(createProductRequest.getBrandId())
                .thenAcceptAsync(product::setBrand);
        // check for category
        var categoryCompletable = findCategoryById(createProductRequest.getCategoryId())
                .thenAcceptAsync(product::setCategory);
        // check for sizes
        var sizesCompletable = findSizesByIds(createProductRequest.getSizeIds())
                .thenAcceptAsync(product::setSizes);

        try {
            CompletableFuture.allOf(brandCompletable, categoryCompletable, sizesCompletable).join();
        } catch (CompletionException e) {
            throw (NotFoundException) e.getCause();
        }

        // var result = cloudinaryImageService.upload(createProductRequest.imageFile().getBytes(), "products",
        //         GenerateRandom.token());
        // product.setImageUrl(result.secureUrl());
        product.setCloudinaryPublicId(String.format("%s/%s", "products", GenerateRandom.token()));

        return productsRepository.save(product);
    }

    @Override
    public Product update(Long id, CreateUpdateProductRequest updateProductRequest) throws Exception {
        var product = this.getProductById(id);
        product.setName(updateProductRequest.getName());
        product.setPrice(updateProductRequest.getPrice());
        product.setDescription(updateProductRequest.getDescription());

        // check for brand
        var brandCompletable = findBrandById(updateProductRequest.getBrandId())
                .thenAcceptAsync(product::setBrand);
        // check for category
        var categoryCompletable = findCategoryById(updateProductRequest.getCategoryId())
                .thenAcceptAsync(product::setCategory);
        // check for sizes
        var sizesCompletable = findSizesByIds(updateProductRequest.getSizeIds())
                .thenAcceptAsync(product::setSizes);

        CompletableFuture.allOf(brandCompletable, categoryCompletable, sizesCompletable).join();

        // var result = cloudinaryImageService.upload(createProductRequest.imageFile().getBytes(), "products",
        //         GenerateRandom.token());
        // product.setImageUrl(result.secureUrl());
        product.setCloudinaryPublicId(String.format("%s/%s", "products", GenerateRandom.token()));

        return productsRepository.save(product);
    }

    @Override
    public void setImageUrl(Long id, String imageUrl) throws Exception {
        var product = this.getProductById(id);
        product.setImageUrl(imageUrl);
        productsRepository.save(product);
    }

    @Override
    public void delete(Long id) throws Exception {
        var product = this.getProductById(id);
        // String publicId = product.getCloudinaryPublicId();

        // // TODO Delete object on cloudinary
        // cloudinaryImageService.delete(publicId);

        productsRepository.delete(product);
    }

    private CompletableFuture<Brand> findBrandById(Long id) throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            log.info("findBrandById({})", id);
            return brandService.findById(id);
        });
    }

    private CompletableFuture<Category> findCategoryById(Long id) throws NotFoundException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("findCategoryById({})", id);
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
                    log.info("findSizeById({})", t);
                    return sizeService.getSizeById(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new NotFoundException("size not found");
                }
            }).collect(Collectors.toList());
        });
    }

}
