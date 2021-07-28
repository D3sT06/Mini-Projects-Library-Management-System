package com.sahin.library_management.controller;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/model")
public class AuthorModelController {

    @Autowired
    private AuthorService authorService;

    @PostMapping("/authors/create")
    public String createAuthor(AuthorEntity author, Model model) {

        authorService.createAuthor(author);
        model.addAttribute("authors", authorService.getAll());

        return "redirect:/model/authors";
    }

    @PostMapping("/authors/update/{barcode}")
    public String updateAuthor(AuthorEntity author, @PathVariable String barcode, Model model) {

        author.setBarcode(authorService.getAuthorById(barcode).getBarcode());
        authorService.updateAuthor(author);
        model.addAttribute("authors", authorService.getAll());

        return "redirect:/model/authors";
    }

    @GetMapping("/authors/delete/{barcode}")
    public String deleteAuthorById(@PathVariable String barcode, Model model) {

        authorService.deleteAuthorById(barcode);
        model.addAttribute("authors", authorService.getAll());

        return "redirect:/model/authors";
    }

    @GetMapping({"authors", "authors.html"})
    public String authors(Model model) {
        model.addAttribute("authors", authorService.getAll());
        return "authors";
    }

    @GetMapping("/authors/new")
    public String showSignUpForm(AuthorEntity author, Model model) {
        model.addAttribute("author", author);
        return "add-author";
    }

    @GetMapping("/authors/edit/{barcode}")
    public String showEditForm(@PathVariable String barcode, Model model) {

        AuthorEntity author = authorService.getAuthorById(barcode);
        model.addAttribute("author", author);
        return "update-author";
    }
}
