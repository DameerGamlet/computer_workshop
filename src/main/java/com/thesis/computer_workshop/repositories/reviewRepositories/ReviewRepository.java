package com.thesis.computer_workshop.repositories.reviewRepositories;

import com.thesis.computer_workshop.models.products.UsbFlashProduct;
import com.thesis.computer_workshop.models.reviews.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long> {
}
