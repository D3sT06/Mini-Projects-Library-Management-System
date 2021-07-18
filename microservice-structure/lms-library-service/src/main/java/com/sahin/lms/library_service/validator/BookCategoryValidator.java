package com.sahin.lms.library_service.validator;

import com.sahin.lms.infra_entity.library.jpa.BookCategoryEntity;
import com.sahin.lms.infra_model.library.model.BookCategory;
import com.sahin.lms.library_service.repository.BookCategoryRepository;
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
        return BookCategory.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        BookCategory category = (BookCategory) o;

        Optional<BookCategoryEntity> queryResult;
        if (category.getId() == null)
            queryResult = categoryRepository.findByName(category.getName());
        else
            queryResult = categoryRepository.findByNameAndIdIsNot(category.getName(), category.getId());

        if (queryResult.isPresent()) {
            errors.rejectValue("name", "ALREADY EXIST", "The book category with same title already exists!");
        }
    }
}
