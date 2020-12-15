package com.sahin.library_management.controller;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.service.BookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BookCategoryModelController {

    @Autowired
    private BookCategoryService categoryService;

    @PostMapping("/categories/create")
    public String createCategory(BookCategoryEntity category, Model model) {

        categoryService.createCategory(category);
        model.addAttribute("categories", categoryService.getAll());

        return "redirect:/categories";
    }

    @PostMapping("/categories/update/{id}")
    public String updateCategory(BookCategoryEntity category, @PathVariable Long id, Model model) {

        category.setId(categoryService.getCategoryById(id).getId());
        categoryService.updateCategory(category);
        model.addAttribute("categories", categoryService.getAll());

        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategoryById(@PathVariable Long id, Model model) {

        categoryService.deleteCategoryById(id);
        model.addAttribute("categories", categoryService.getAll());

        return "redirect:/categories";
    }

    @GetMapping({"categories", "categories.html"})
    public ModelAndView categories() {
        ModelAndView modelAndView = new ModelAndView("categories");
        modelAndView.addObject("categories", categoryService.getAll());
        return modelAndView;
    }

//    @GetMapping({"categories", "categories.html"})
//    public String categories(Model model) {
//        model.addAttribute("categories", categoryService.getAll());
//        return "categories";
//    }

    @GetMapping("/categories/new")
    public String showSignUpForm(BookCategoryEntity category, Model model) {
        model.addAttribute("category", category);
        return "add-category";
    }

    @GetMapping("/categories/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        BookCategoryEntity entity = categoryService.getCategoryById(id);
        model.addAttribute("category", entity);
        return "update-category";
    }
}
