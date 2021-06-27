package com.sahin.library_management.controller;

import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.infra.projections.AuthorProjections;
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
    public String createAuthor(Author author, Model model) {

        authorService.createAuthor(author);
        model.addAttribute("authors", authorService.getAll());

        return "redirect:/model/authors";
    }

    @PostMapping("/authors/update/{id}")
    public String updateAuthor(Author author, @PathVariable Long id, Model model) {

        author.setId(authorService.getAuthorById(id).getId());
        authorService.updateAuthor(author);
        model.addAttribute("authors", authorService.getAll());

        return "redirect:/model/authors";
    }

    @GetMapping("/authors/delete/{id}")
    public String deleteAuthorById(@PathVariable Long id, Model model) {

        authorService.deleteAuthorById(id);
        model.addAttribute("authors", authorService.getAll());

        return "redirect:/model/authors";
    }

    @GetMapping({"authors", "authors.html"})
    public String authors(Model model) {
        model.addAttribute("authors", authorService.getAll());
        return "authors";
    }

    @GetMapping("/authors/new")
    public String showSignUpForm(Author author, Model model) {
        model.addAttribute("author", author);
        return "add-author";
    }

    @GetMapping("/authors/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        AuthorProjections.AuthorView authorView = authorService.getAuthorById(id);
        model.addAttribute("author", authorView);
        return "update-author";
    }
}
