package com.sahin.lms.library_service.controller;

import com.sahin.lms.infra_aop.annotation.LogExecutionTime;
import com.sahin.lms.infra_model.library.model.BookCategory;
import com.sahin.lms.library_service.projection.CategoryProjections;
import com.sahin.lms.library_service.service.BookCategoryService;
import com.sahin.lms.library_service.swagger.controller.BookCategorySwaggerApi;
import com.sahin.lms.library_service.validator.BookCategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/categories")
@LogExecutionTime
public class BookCategoryController implements BookCategorySwaggerApi {

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
    public ResponseEntity<CategoryProjections.CategoryView> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }


    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("getAll")
    public ResponseEntity<List<CategoryProjections.CategoryView>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }
}
