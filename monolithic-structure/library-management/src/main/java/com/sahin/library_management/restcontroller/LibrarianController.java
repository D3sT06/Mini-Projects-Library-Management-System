package com.sahin.library_management.restcontroller;

import com.sahin.library_management.swagger.controller.LibrarianSwaggerApi;
import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.model.account.Librarian;
import com.sahin.library_management.service.LibrarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/librarians")
@LogExecutionTime
public class LibrarianController implements LibrarianSwaggerApi {

    @Autowired
    private LibrarianService librarianService;

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("create")
    public ResponseEntity<Void> createLibrarian(@RequestBody @Valid Librarian librarian) {
        librarianService.createLibrarian(librarian);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<Void> updateLibrarian(@RequestBody @Valid Librarian librarian) {
        librarianService.updateLibrarian(librarian);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @DeleteMapping("delete/{barcode}")
    public ResponseEntity<Void> deleteLibrarianByBarcode(@PathVariable String barcode) {
        librarianService.deleteLibrarianByBarcode(barcode);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("get/{barcode}")
    public ResponseEntity<Librarian> getLibrarianByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(librarianService.getLibrarianByBarcode(barcode));
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("getAll")
    public ResponseEntity<List<Librarian>> getAll() {
        return ResponseEntity.ok(librarianService.getAll());
    }
}
