package com.example.demo.repositories;

import com.example.demo.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findBySection(String section);
    List<MenuItem> findBySectionAndCategoryId(String section, Long categoryId);

}
