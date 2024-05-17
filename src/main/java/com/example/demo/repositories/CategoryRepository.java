package com.example.demo.repositories;


import com.example.demo.model.Category;
import com.example.demo.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Subscriber> findByPromoCode(String promoCode);
}
