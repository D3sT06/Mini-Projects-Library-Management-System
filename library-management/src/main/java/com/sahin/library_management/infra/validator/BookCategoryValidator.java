package com.sahin.library_management.infra.validator;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.infra.model.book.BookCategory;
import com.sahin.library_management.repository.BookCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class BookCategoryValidator implements Validator {

    @Autowired
    private BookCategoryRepository categoryRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return BookCategory.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        BookCategory category = (BookCategory) o;

        Optional<BookCategoryEntity> queryResult = categoryRepository.findByName(category.getName());

        if (queryResult.isPresent()) {
            errors.rejectValue("name", "ALREADY EXIST", "The book category with same title already exists!");
        }
    }
}
