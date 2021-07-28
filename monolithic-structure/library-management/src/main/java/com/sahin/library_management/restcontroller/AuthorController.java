package com.sahin.library_management.restcontroller;

import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
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
    public ResponseEntity<AuthorEntity> createAuthor(@RequestBody AuthorEntity author) {
        AuthorEntity createdAuthor = authorService.createAuthor(author);
        return ResponseEntity.ok(createdAuthor);
    }

    @PutMapping("update")
    public ResponseEntity<Void> updateAuthor(@RequestBody AuthorEntity author) {
        authorService.updateAuthor(author);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{authorId}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable Long authorId) {
        authorService.deleteAuthorById(authorId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get/{authorId}")
    public ResponseEntity<AuthorEntity> getAuthorById(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorById(authorId));
    }

    @GetMapping("getAll")
    public ResponseEntity<List<AuthorEntity>> getAll() {
        return ResponseEntity.ok(authorService.getAll());
    }
}
