package com.sahin.library_management.service;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.entity.LibrarianEntity;
import com.sahin.library_management.infra.entity.LibraryCardEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.Librarian;
import com.sahin.library_management.mapper.LibrarianMapper;
import com.sahin.library_management.repository.LibrarianRepository;
import com.sahin.library_management.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@LogExecutionTime
public class LibrarianService {

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private LibrarianMapper librarianMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void createLibrarian(Librarian librarian) {
        if (librarian.getLibraryCard() != null || librarian.getId() != null)
            throw new MyRuntimeException("Librarian to be created cannot have a library card or an id.", HttpStatus.BAD_REQUEST);

        LibrarianEntity entity = librarianMapper.toEntity(librarian);

        entity.setLibraryCard(new LibraryCardEntity());
        entity.getLibraryCard().setIssuedAt(Instant.now().toEpochMilli());
        entity.getLibraryCard().setActive(true);
        entity.getLibraryCard().setAccountFor(AccountFor.LIBRARIAN);

        String password = PasswordUtil.createRandomPassword();
        entity.getLibraryCard().setPassword(passwordEncoder.encode(password));

        librarianRepository.save(entity);
    }

    @Transactional
    public void updateLibrarian(Librarian librarian) {
        if (librarian.getId() == null)
            throw new MyRuntimeException("Librarian to be created must have an id.", HttpStatus.BAD_REQUEST);

        Optional<LibrarianEntity> optionalEntity = librarianRepository.findById(librarian.getId());

        if (!optionalEntity.isPresent())
            throw new MyRuntimeException("Librarian with id \"" + librarian.getId() + "\" not exist!", HttpStatus.BAD_REQUEST);

        LibrarianEntity entity = librarianMapper.toEntity(librarian);
        entity.setLibraryCard(optionalEntity.get().getLibraryCard());

        librarianRepository.save(entity);
    }

    @Transactional
    public void deleteLibrarianByBarcode(String barcode) {
        Optional<LibrarianEntity> optionalEntity = librarianRepository.findByLibraryCardBarcode(barcode);

        if (!optionalEntity.isPresent())
            throw new MyRuntimeException("Librarian with card barcode \"" + barcode + "\" not exist!", HttpStatus.BAD_REQUEST);

        librarianRepository.deleteByLibraryCardBarcode(barcode);
    }

    @Transactional
    public Librarian getLibrarianByBarcode(String barcode) {
        LibrarianEntity entity = librarianRepository
                .findByLibraryCardBarcode(barcode)
                .orElseThrow(()-> new MyRuntimeException("Librarian with card barcode " + barcode + " not exist!", HttpStatus.BAD_REQUEST));

        return librarianMapper.toModel(entity);
    }

    @Transactional
    public List<Librarian> getAll() {
        List<LibrarianEntity> entities = librarianRepository
                .findAll();

        return librarianMapper.toModels(entities);
    }


}
