package com.phincon.laza.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.phincon.laza.config.BrandDataConfig;
import com.phincon.laza.model.entity.Brand;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@Import({BrandDataConfig.class})
public class BrandRepositoryTest {

  @Autowired
  private BrandRepository repository;

  @Autowired
  @Qualifier("brand.all")
  private final List<Brand> brands;

  @Autowired
  @Qualifier("brand.one")
  private final Brand brandOne;

  @Autowired
  @Qualifier("brand.one.dup")
  private final Brand brandOneDup;

  @BeforeEach
  public void init() {
    brands.forEach(repository::save);
  }

  @Test
  @DisplayName("get all brand with pagination, page 1 with size of 4")
  public void getAllBrandPaginationPage1Size4() {
    Pageable pageable = PageRequest.of(1, 4);
    Page<Brand> result = repository.findAll(pageable);
    List<Brand> expected = brands.subList(4, 6);
    List<Brand> actual = result.toList();

    assertEquals(1, result.getNumber());
    assertEquals(4, result.getSize());
    assertEquals(2, result.getNumberOfElements());
    assertTrue(CollectionUtils.isEqualCollection(expected, actual));
  }

  @Test
  @DisplayName("Get one brand with specific id")
  public void getBrandSpecificId_found() {
    long id = 2;
    int index = 2;
    
    Brand expected = brands.get(index); // new Brand(3l, "ADIDAS", "logoUrl", null),
    Optional<Brand> actual = repository.findById(id);
    
    assertTrue(actual.isPresent());
    assertEquals(expected, actual.get());
  }

  @Test
  @DisplayName("Get one brand with specific id but not found")
  public void getBrandSpecificId_notFound() {
    long id = 2;
    int index = 2;
    
    Brand expected = brands.get(index); // new Brand(3l, "ADIDAS", "logoUrl", null),
    Optional<Brand> actual = repository.findById(id);
    
    assertTrue(actual.isEmpty());
  }

  @Test
  @DisplayName("Add a brand should return same data and count should be 7")
  public void addNewBrand_data() {
    Brand output = repository.save(brandOne);
    
    final int countExpected = 7;
    int countActual = repository.count();
    
    assertEquals(countExpected, countActual);
    assertEquals("3Second", output.getName());
    assumeTrue(7l == output.getId(), "newly created id does not equal to 7l but carry on...");
  }

  @Test
  @DisplayName("Add a brand with duplicate name should throw exception")
  public void addNewBrandSameName_exception() {
    assertThrows(DataIntegrityViolationException.class, () -> repository.save(brandOneDup));
  }

  @Test
  @DisplayName("Update a brand should return the updated data")
  public void updateBrand_data() {
    Long id = 1l;
    Brand toBeUpdated = brands.get(0);
    toBeUpdated.setLogoUrl("www.example.com");
    Brand updated = repository.save(toBeUpdated);
    
    assertEquals(toBeUpdated.getLogoUrl(), updated.getLogoUrl());
    assertEquals(toBeUpdated.getName(), updated.getName());
  }

  @Test
  @DisplayName("Delete a brand should ok and count should be 5")
  public void deleteBrand_ok() {
    Long id = 1l;
    Brand toBeDeleted = repository.findById(id).get();
    repository.delete(toBeDeleted);
    assertEquals(5, repository.count());
  }
}
