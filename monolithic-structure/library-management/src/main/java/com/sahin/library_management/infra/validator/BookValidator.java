package com.sahin.library_management.infra.validator;

import com.sahin.library_management.infra.entity.jpa.BookEntity;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.repository.jpa.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;


@Component
public class BookValidator implements Validator {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;

        Optional<BookEntity> queryResult;
        if (book.getId() == null)
            queryResult = bookRepository.findByTitleAndAuthorId(
                    book.getTitle(), book.getAuthor().getId());
        else
            queryResult = bookRepository.findByTitleAndAuthorIdAndIdIsNot(
                    book.getTitle(), book.getAuthor().getId(), book.getId());

        if (queryResult.isPresent()) {
            errors.reject("ALREADY EXIST", "The book with same title and author already exists!");
        }
    }
}
