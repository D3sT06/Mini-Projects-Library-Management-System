package com.sahin.library_management.restcontroller;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping("create")
    public ResponseEntity<Void> createAuthor(@RequestBody AuthorEntity author) {
        authorService.createAuthor(author);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update")
    public ResponseEntity<Void> updateAuthor(@RequestBody AuthorEntity author) {
        authorService.updateAuthor(author);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{barcode}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable String barcode) {
        authorService.deleteAuthorById(barcode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get/{barcode}")
    public ResponseEntity<AuthorEntity> getAuthorById(@PathVariable String barcode) {
        return ResponseEntity.ok(authorService.getAuthorById(barcode));
    }

    @GetMapping("getAll")
    public ResponseEntity<List<AuthorEntity>> getAll() {
        return ResponseEntity.ok(authorService.getAll());
    }
}
