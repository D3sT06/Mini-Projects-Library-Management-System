package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.AccountEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.enums.LoginType;
import com.sahin.library_management.infra.model.account.AccountLoginType;
import com.sahin.library_management.infra.model.account.Librarian;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.mapper.AccountMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.jpa.AccountRepository;
import com.sahin.library_management.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibrarianService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Transactional
    public void createLibrarian(Librarian librarian) {
        String password = PasswordUtil.createRandomPassword();

        LibraryCard card = new LibraryCard();
        card.setIssuedAt(Instant.now().toEpochMilli());
        card.setActive(true);
        card.setAccountFor(AccountFor.LIBRARIAN);
        card.setPassword(password);

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
        Optional<AccountEntity> optionalEntity = accountRepository.findById(librarian.getId());
        AccountEntity entity = accountMapper.toEntity(librarian, new CyclePreventiveContext());
        entity.setLibraryCard(optionalEntity.get().getLibraryCard());
        accountRepository.save(entity);
    }

    @Transactional
    public void deleteLibrarianByBarcode(String barcode) {
        accountRepository.deleteByLibraryCardBarcode(barcode);
    }

    @Transactional
    public Librarian getLibrarianByBarcode(String barcode) {
        AccountEntity entity = accountRepository
                .findByLibraryCardBarcode(barcode)
                .get();

        return (Librarian) accountMapper.toModel(entity);
    }

    @Transactional
    public List<Librarian> getAll() {
        List<AccountEntity> entities = accountRepository
                .findAll();

        List<AccountEntity> filteredEntities = entities.stream()
                .filter(accountEntity -> accountEntity.getLibraryCard().getAccountFor().equals(AccountFor.LIBRARIAN))
                .collect(Collectors.toList());

        return accountMapper.toLibrarianModels(filteredEntities);
    }
}
