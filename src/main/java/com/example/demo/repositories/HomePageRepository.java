package com.example.demo.repositories;


import com.example.demo.model.HomePage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomePageRepository extends JpaRepository<HomePage, Long> {
}