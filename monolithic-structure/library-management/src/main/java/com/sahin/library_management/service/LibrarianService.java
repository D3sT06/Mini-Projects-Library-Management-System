package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.AccountEntity;
import com.sahin.library_management.infra.entity.jpa.AccountLoginTypeEntity;
import com.sahin.library_management.infra.entity.jpa.LibraryCardEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.enums.LoginType;
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

    @Transactional
    public void createLibrarian(AccountEntity librarian) {
        String password = PasswordUtil.createRandomPassword();

        LibraryCardEntity card = new LibraryCardEntity();
        card.setIssuedAt(Instant.now().toEpochMilli());
        card.setActive(true);
        card.setAccountFor(AccountFor.LIBRARIAN);
        card.setPassword(password);

        AccountLoginTypeEntity passwordType = new AccountLoginTypeEntity();
        passwordType.setType(LoginType.PASSWORD);
        passwordType.setLibraryCard(card);

        card.setLoginTypes(new HashSet<>(Arrays.asList(passwordType)));
        librarian.setLibraryCard(card);

        accountRepository.save(librarian);
    }

    @Transactional
    public void updateLibrarian(AccountEntity librarian) {
        Optional<AccountEntity> optionalEntity = accountRepository.findById(librarian.getId());
        librarian.setLibraryCard(optionalEntity.get().getLibraryCard());
        accountRepository.save(librarian);
    }

    @Transactional
    public void deleteLibrarianByBarcode(String barcode) {
        accountRepository.deleteByLibraryCardBarcode(barcode);
    }

    @Transactional
    public AccountEntity getLibrarianByBarcode(String barcode) {
        return accountRepository
                .findByLibraryCardBarcode(barcode)
                .get();
    }

    @Transactional
    public List<AccountEntity> getAll() {
        List<AccountEntity> entities = accountRepository
                .findAll();

        return entities.stream()
                .filter(accountEntity -> accountEntity.getLibraryCard().getAccountFor().equals(AccountFor.LIBRARIAN))
                .collect(Collectors.toList());
    }
}
