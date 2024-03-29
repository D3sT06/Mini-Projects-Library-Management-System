package com.sahin.lms.library_service.controller;

import com.sahin.lms.infra_aop.annotation.LogExecutionTime;
import com.sahin.lms.infra_model.library.model.Author;
import com.sahin.lms.library_service.projection.AuthorProjections;
import com.sahin.lms.library_service.service.AuthorService;
import com.sahin.lms.library_service.swagger.controller.AuthorSwaggerApi;
import com.sahin.lms.library_service.validator.AuthorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/authors")
@LogExecutionTime
public class AuthorController implements AuthorSwaggerApi {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorValidator authorValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(authorValidator);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("create")
    public ResponseEntity<Author> createAuthor(@RequestBody @Valid Author author) {
        Author createdAuthor = authorService.createAuthor(author);
        return ResponseEntity.ok(createdAuthor);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<Void> updateAuthor(@RequestBody @Valid Author author) {
        authorService.updateAuthor(author);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @DeleteMapping("delete/{authorId}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable Long authorId) {
        authorService.deleteAuthorById(authorId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    @GetMapping("get/{authorId}")
    public ResponseEntity<AuthorProjections.AuthorView> getAuthorById(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorById(authorId));
    }


    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("getAll")
    public ResponseEntity<List<AuthorProjections.AuthorView>> getAll() {
        return ResponseEntity.ok(authorService.getAll());
    }
}
