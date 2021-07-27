package com.sahin.library_management.service;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.entity.jpa.AccountEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.enums.LoginType;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.AccountLoginType;
import com.sahin.library_management.infra.model.account.Librarian;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.mapper.AccountMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.jpa.AccountRepository;
import com.sahin.library_management.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@LogExecutionTime
public class LibrarianService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void createLibrarian(Librarian librarian) {
        if (librarian.getLibraryCard() != null || librarian.getId() != null)
            throw new MyRuntimeException("Librarian to be created cannot have a library card or an id.", HttpStatus.BAD_REQUEST);

        String password = PasswordUtil.createRandomPassword();

        LibraryCard card = new LibraryCard();
        card.setIssuedAt(Instant.now().toEpochMilli());
        card.setActive(true);
        card.setAccountFor(AccountFor.LIBRARIAN);
        card.setPassword(passwordEncoder.encode(password));

        AccountLoginType passwordType = new AccountLoginType();
        passwordType.setType(LoginType.PASSWORD);
        passwordType.setLibraryCard(card);

        card.setLoginTypes(new HashSet<>(Arrays.asList(passwordType)));
        librarian.setLibraryCard(card);

        AccountEntity entity = accountMapper.toEntity(librarian, new CyclePreventiveContext());
        accountRepository.save(entity);
    }

    @Transactional
    public void updateLibrarian(Librarian librarian) {
        if (librarian.getId() == null)
            throw new MyRuntimeException("Librarian to be created must have an id.", HttpStatus.BAD_REQUEST);

        Optional<AccountEntity> optionalEntity = accountRepository.findById(librarian.getId());

        if (!optionalEntity.isPresent())
            throw new MyRuntimeException("Librarian with id \"" + librarian.getId() + "\" not exist!", HttpStatus.BAD_REQUEST);

        if (!optionalEntity.get().getLibraryCard().getAccountFor().equals(AccountFor.LIBRARIAN))
            throw new MyRuntimeException("The account is not for a librarian", HttpStatus.BAD_REQUEST);

        AccountEntity entity = accountMapper.toEntity(librarian, new CyclePreventiveContext());
        entity.setLibraryCard(optionalEntity.get().getLibraryCard());
        accountRepository.save(entity);
    }

    @Transactional
    public void deleteLibrarianByBarcode(String barcode) {
        Optional<AccountEntity> optionalEntity = accountRepository.findByLibraryCardBarcode(barcode);

        if (!optionalEntity.isPresent())
            throw new MyRuntimeException("Librarian with card barcode \"" + barcode + "\" not exist!", HttpStatus.BAD_REQUEST);

        if (!optionalEntity.get().getLibraryCard().getAccountFor().equals(AccountFor.LIBRARIAN))
            throw new MyRuntimeException("The account is not for a librarian", HttpStatus.BAD_REQUEST);

        accountRepository.deleteByLibraryCardBarcode(barcode);
    }

    @Transactional
    public Librarian getLibrarianByBarcode(String barcode) {
        AccountEntity entity = accountRepository
                .findByLibraryCardBarcode(barcode)
                .orElseThrow(()-> new MyRuntimeException("Librarian with card barcode " + barcode + " not exist!", HttpStatus.BAD_REQUEST));

        if (!(accountMapper.toModel(entity) instanceof Librarian))
            throw new MyRuntimeException("The account is not for a librarian", HttpStatus.BAD_REQUEST);

        return (Librarian) accountMapper.toModel(entity);
    }

    @Transactional
    public List<Librarian> getAll() {
        List<AccountEntity> entities = accountRepository
                .getAll(AccountFor.LIBRARIAN);

        return accountMapper.toLibrarianModels(entities);
    }
}
