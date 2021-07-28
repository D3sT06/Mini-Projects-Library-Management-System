package com.sahin.library_management.bootstrap;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookLoader implements Loader<BookEntity> {

    @Autowired
    private LibraryRepository libraryRepository;

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
        book1.setCategories(new HashSet<>(Arrays.asList(categories.get(0), categories.get(3))));
        book1.setPublicationDate(LocalDate.of(1990, 10, 1));

        BookEntity book2 = new BookEntity();
        book2.setAuthor(authors.get(1));
        book2.setTitle("Fareler ve İnsanlar");
        book2.setCategories(new HashSet<>(Arrays.asList(categories.get(0))));
        book2.setPublicationDate(LocalDate.of(1990, 10, 1));

        BookEntity book3 = new BookEntity();
        book3.setAuthor(authors.get(2));
        book3.setTitle("Hayvan Çiftliği");
        book3.setCategories(new HashSet<>(Arrays.asList(categories.get(0))));
        book3.setPublicationDate(LocalDate.of(1995, 9, 1));

        BookEntity book4 = new BookEntity();
        book4.setAuthor(authors.get(2));
        book4.setTitle("1984");
        book4.setCategories(new HashSet<>(Arrays.asList(categories.get(0))));
        book4.setPublicationDate(LocalDate.of(1985, 3, 1));

        BookEntity book5 = new BookEntity();
        book5.setAuthor(authors.get(3));
        book5.setTitle("Zamanın Kısa Tarihi");
        book5.setCategories(new HashSet<>(Arrays.asList(categories.get(4))));
        book5.setPublicationDate(LocalDate.of(2010, 7, 1));

        libraryRepository.save(book1);
        libraryRepository.save(book2);
        libraryRepository.save(book3);
        libraryRepository.save(book4);
        libraryRepository.save(book5);
    }

    @Override
    public void clearDb() {
        List<BookEntity> entities = this.getAll();
        entities.forEach(entity -> libraryRepository.deleteById(entity.getBarcode(), BookEntity.class));
    }

    public List<BookEntity> getAll() {
        return libraryRepository.findAll(BookEntity.class)
                .stream()
                .map(entity -> (BookEntity) entity)
                .collect(Collectors.toList());
    }
}
