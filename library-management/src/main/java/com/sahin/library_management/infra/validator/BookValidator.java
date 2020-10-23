package com.sahin.library_management.infra.validator;

import com.sahin.library_management.infra.entity.BookEntity;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.repository.BookRepository;
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
        return Book.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;

        Optional<BookEntity> queryResult = bookRepository.findByTitleAndAuthorIdAndCategoryId(
                book.getTitle(), book.getAuthor().getId(), book.getCategory().getId());

        if (queryResult.isPresent()) {
            errors.reject("ALREADY EXIST", "The book with same title, author and category already exists!");
        }
    }
}
