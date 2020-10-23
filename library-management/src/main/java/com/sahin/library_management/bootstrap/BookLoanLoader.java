package com.sahin.library_management.bootstrap;

import com.sahin.library_management.infra.entity.BookLoaningEntity;
import com.sahin.library_management.repository.BookLoaningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookLoanLoader implements Loader<BookLoaningEntity> {

    @Autowired
    private BookLoaningRepository loaningRepository;

    @Override
    public void loadDb() {
    }

    @Override
    public void clearDb() {
        loaningRepository.deleteAll();
    }

    @Override
    public List<BookLoaningEntity> getAll() {
        return loaningRepository.findAll();
    }
}
