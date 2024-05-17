package com.example.demo.service;
import com.example.demo.model.Booking;

import com.example.demo.dto.MenuItemDto;
import com.example.demo.model.Category;
import com.example.demo.model.MenuItem;
import com.example.demo.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import com.example.demo.repositories.MenuItemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public MenuItemDto getMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + id));
        return convertToDto(menuItem);
    }
    @Override
    public MenuItemDto addMenuItem(MenuItemDto menuItemDto) {
        MenuItem menuItem = convertToEntity(menuItemDto);
        menuItem = menuItemRepository.save(menuItem);
        return convertToDto(menuItem);
    }
    @Override
    public List<MenuItemDto> getMenuItemsBySection(String section) {
        List<MenuItem> menuItems = menuItemRepository.findBySection(section);
        return menuItems.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + id));
        menuItem.setName(menuItemDto.getName());
        menuItem.setDescription(menuItemDto.getDescription());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setImageUrl(menuItemDto.getImageUrl()); // Existing line
        menuItem.setSection(menuItemDto.getSection()); // Add this line
        menuItem = menuItemRepository.save(menuItem);
        return convertToDto(menuItem);
    }

    @Override
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + id));
        menuItemRepository.delete(menuItem);
    }



    private MenuItemDto convertToDto(MenuItem menuItem) {
        MenuItemDto menuItemDto = new MenuItemDto();
        menuItemDto.setId(menuItem.getId());
        menuItemDto.setName(menuItem.getName());
        menuItemDto.setDescription(menuItem.getDescription());
        menuItemDto.setPrice(menuItem.getPrice());
        menuItemDto.setImageUrl(menuItem.getImageUrl());
        menuItemDto.setSection(menuItem.getSection());

        if (menuItem.getCategory() != null) {
            menuItemDto.setCategoryId(menuItem.getCategory().getId());
            menuItemDto.setCategoryName(menuItem.getCategory().getName());
        }

        return menuItemDto;
    }

    @Override
    public List<MenuItemDto> getMenuItemsBySectionAndCategory(String section, Long categoryId) {
        if (categoryId == null) {
            return menuItemRepository.findBySection(section).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else {
            return menuItemRepository.findBySectionAndCategoryId(section, categoryId).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
    }

    private MenuItem convertToEntity(MenuItemDto menuItemDto) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemDto.getName());
        menuItem.setDescription(menuItemDto.getDescription());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setImageUrl(menuItemDto.getImageUrl());
        menuItem.setSection(menuItemDto.getSection());

        // Fetch and set the Category
        if (menuItemDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(menuItemDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + menuItemDto.getCategoryId()));
            menuItem.setCategory(category);
        }

        return menuItem;
    }

    @Override
    public List<MenuItemDto> getAllMenuItems() {
        List<MenuItem> items = menuItemRepository.findAll();
        return items.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



}





