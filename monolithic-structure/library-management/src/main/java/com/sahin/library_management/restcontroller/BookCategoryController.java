package com.sahin.library_management.restcontroller;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.service.BookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class BookCategoryController {

    @Autowired
    private BookCategoryService categoryService;

    @PostMapping("create")
    public ResponseEntity<BookCategoryEntity> createCategory(@RequestBody BookCategoryEntity bookCategory) {
        BookCategoryEntity createdCategory = categoryService.createCategory(bookCategory);
        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("update")
    public ResponseEntity<Void> updateCategory(@RequestBody BookCategoryEntity bookCategory) {
        categoryService.updateCategory(bookCategory);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{categoryId}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get/{categoryId}")
    public ResponseEntity<BookCategoryEntity> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @GetMapping("getAll")
    public ResponseEntity<List<BookCategoryEntity>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }
}
