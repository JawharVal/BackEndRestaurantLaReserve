package com.example.demo.repositories;


import com.example.demo.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, String> {

    Optional<Subscriber> findByPromoCode(String promoCode);
}
