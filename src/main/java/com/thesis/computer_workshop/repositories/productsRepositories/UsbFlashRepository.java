package com.thesis.computer_workshop.repositories.productsRepositories;

import com.thesis.computer_workshop.models.products.UsbFlashProduct;
import org.springframework.data.repository.CrudRepository;

public interface UsbFlashRepository extends CrudRepository<UsbFlashProduct, Long> {
}
