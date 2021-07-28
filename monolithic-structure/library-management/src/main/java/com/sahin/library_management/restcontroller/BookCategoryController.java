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
    public ResponseEntity<Void> createCategory(@RequestBody BookCategoryEntity bookCategory) {
        categoryService.createCategory(bookCategory);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update")
    public ResponseEntity<Void> updateCategory(@RequestBody BookCategoryEntity bookCategory) {
        categoryService.updateCategory(bookCategory);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{barcode}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable String barcode) {
        categoryService.deleteCategoryById(barcode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get/{barcode}")
    public ResponseEntity<BookCategoryEntity> getCategoryById(@PathVariable String barcode) {
        return ResponseEntity.ok(categoryService.getCategoryById(barcode));
    }

    @GetMapping("getAll")
    public ResponseEntity<List<BookCategoryEntity>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }
}
