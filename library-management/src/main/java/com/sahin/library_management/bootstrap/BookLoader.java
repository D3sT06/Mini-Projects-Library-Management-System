package com.sahin.library_management.bootstrap;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BookLoader implements Loader<BookEntity> {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorLoader authorLoader;

    @Autowired
    private CategoryLoader categoryLoader;

    @Override
    public void loadDb() {
        List<AuthorEntity> authors = authorLoader.getAll();
        List<BookCategoryEntity> categories = categoryLoader.getAll();

        BookEntity book1 = new BookEntity();
        book1.setAuthor(authors.get(0));
        book1.setTitle("Körlük");
        book1.setCategory(categories.get(0));
        book1.setPublicationDate(LocalDate.of(1990, 10, 1));

        BookEntity book2 = new BookEntity();
        book2.setAuthor(authors.get(1));
        book2.setTitle("Fareler ve İnsanlar");
        book2.setCategory(categories.get(0));
        book2.setPublicationDate(LocalDate.of(1990, 10, 1));

        BookEntity book3 = new BookEntity();
        book3.setAuthor(authors.get(2));
        book3.setTitle("Hayvan Çiftliği");
        book3.setCategory(categories.get(0));
        book3.setPublicationDate(LocalDate.of(1995, 9, 1));

        BookEntity book4 = new BookEntity();
        book4.setAuthor(authors.get(2));
        book4.setTitle("1984");
        book4.setCategory(categories.get(0));
        book4.setPublicationDate(LocalDate.of(1985, 3, 1));

        BookEntity book5 = new BookEntity();
        book5.setAuthor(authors.get(3));
        book5.setTitle("Zamanın Kısa Tarihi");
        book5.setCategory(categories.get(4));
        book5.setPublicationDate(LocalDate.of(2010, 7, 1));

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        bookRepository.save(book4);
        bookRepository.save(book5);
    }

    @Override
    public void clearDb() {
        bookRepository.deleteAll();
    }

    @Override
    public List<BookEntity> getAll() {
        return bookRepository.findAll();
    }
}
