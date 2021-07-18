package com.sahin.lms.library_service.validator;

import com.sahin.lms.infra_entity.library.jpa.AuthorEntity;
import com.sahin.lms.infra_model.library.model.Author;
import com.sahin.lms.library_service.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class AuthorValidator implements Validator {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return Author.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Author author = (Author) o;

        Optional<AuthorEntity> queryResult;
        if (author.getId() == null)
            queryResult = authorRepository.findByNameAndSurname(
                    author.getName(), author.getSurname());
        else
            queryResult = authorRepository.findByNameAndSurnameAndIdIsNot(
                    author.getName(), author.getSurname(), author.getId());

        if (queryResult.isPresent()) {
            errors.reject("ALREADY EXIST", "The author with same name and surname already exists!");
        }
    }
}
