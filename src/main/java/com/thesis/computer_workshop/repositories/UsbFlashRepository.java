package com.thesis.computer_workshop.repositories;

import com.thesis.computer_workshop.models.UsbFlashProduct;
import org.springframework.data.repository.CrudRepository;

public interface UsbFlashRepository extends CrudRepository<UsbFlashProduct, Long> {
}
