package com.example.demo.service;

import com.example.demo.dto.MenuItemDto;

import java.util.List;

public interface MenuItemService {
    MenuItemDto getMenuItem(Long id);
    List<MenuItemDto> getMenuItemsBySectionAndCategory(String section, Long categoryId);
    List<MenuItemDto> getAllMenuItems();
    MenuItemDto addMenuItem(MenuItemDto menuItemDto);
    List<MenuItemDto> getMenuItemsBySection(String section);
    MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto);
    void deleteMenuItem(Long id);
}
