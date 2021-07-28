package com.sahin.library_management.controller;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.service.BookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/model")
public class BookCategoryModelController {

    @Autowired
    private BookCategoryService categoryService;

    @PostMapping("/categories/create")
    public String createCategory(BookCategoryEntity category, Model model) {

        categoryService.createCategory(category);
        model.addAttribute("categories", categoryService.getAll());

        return "redirect:/model/categories";
    }

    @PostMapping("/categories/update/{barcode}")
    public String updateCategory(BookCategoryEntity category, @PathVariable String barcode, Model model) {

        category.setBarcode(categoryService.getCategoryById(barcode).getBarcode());
        categoryService.updateCategory(category);
        model.addAttribute("categories", categoryService.getAll());

        return "redirect:/model/categories";
    }

    @GetMapping("/categories/delete/{barcode}")
    public String deleteCategoryById(@PathVariable String barcode, Model model) {

        categoryService.deleteCategoryById(barcode);
        model.addAttribute("categories", categoryService.getAll());

        return "redirect:/model/categories";
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

    @GetMapping("/categories/edit/{barcode}")
    public String showEditForm(@PathVariable String barcode, Model model) {

        BookCategoryEntity category = categoryService.getCategoryById(barcode);
        model.addAttribute("category", category);
        return "update-category";
    }
}
