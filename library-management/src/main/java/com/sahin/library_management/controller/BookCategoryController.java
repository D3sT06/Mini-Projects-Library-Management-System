package com.sahin.library_management.controller;

import com.sahin.library_management.infra.model.book.BookCategory;
import com.sahin.library_management.infra.validator.BookCategoryValidator;
import com.sahin.library_management.service.BookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/categories")
public class BookCategoryController {

    @Autowired
    private BookCategoryService categoryService;

    @Autowired
    private BookCategoryValidator bookCategoryValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(bookCategoryValidator);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("create")
    public ResponseEntity<BookCategory> createCategory(@RequestBody @Valid BookCategory bookCategory) {
        BookCategory createdCategory = categoryService.createCategory(bookCategory);
        return ResponseEntity.ok(createdCategory);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<Void> updateCategory(@RequestBody @Valid BookCategory bookCategory) {
        categoryService.updateCategory(bookCategory);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @DeleteMapping("delete/{categoryId}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    @GetMapping("get/{categoryId}")
    public ResponseEntity<BookCategory> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }


    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("getAll")
    public ResponseEntity<List<BookCategory>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }
}
