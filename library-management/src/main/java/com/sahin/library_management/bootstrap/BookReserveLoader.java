package com.sahin.library_management.bootstrap;

import com.sahin.library_management.infra.entity.jpa.BookReservingEntity;
import com.sahin.library_management.repository.jpa.BookReservingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookReserveLoader implements Loader<BookReservingEntity> {


    @Autowired
    private BookReservingRepository reservingRepository;

    @Override
    public void loadDb() {

    }

    @Override
    public void clearDb() {
        reservingRepository.deleteAll();
    }

    @Override
    public List<BookReservingEntity> getAll() {
        return reservingRepository.findAll();
    }
}
