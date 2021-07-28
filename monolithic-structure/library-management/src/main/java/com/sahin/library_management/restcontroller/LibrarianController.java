package com.sahin.library_management.restcontroller;

import com.sahin.library_management.infra.model.account.Librarian;
import com.sahin.library_management.service.LibrarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/librarians")
public class LibrarianController {

    @Autowired
    private LibrarianService librarianService;

    @PostMapping("create")
    public ResponseEntity<Void> createLibrarian(@RequestBody Librarian librarian) {
        librarianService.createLibrarian(librarian);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update")
    public ResponseEntity<Void> updateLibrarian(@RequestBody Librarian librarian) {
        librarianService.updateLibrarian(librarian);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{barcode}")
    public ResponseEntity<Void> deleteLibrarianByBarcode(@PathVariable String barcode) {
        librarianService.deleteLibrarianByBarcode(barcode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get/{barcode}")
    public ResponseEntity<Librarian> getLibrarianByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(librarianService.getLibrarianByBarcode(barcode));
    }

    @GetMapping("getAll")
    public ResponseEntity<List<Librarian>> getAll() {
        return ResponseEntity.ok(librarianService.getAll());
    }
}
