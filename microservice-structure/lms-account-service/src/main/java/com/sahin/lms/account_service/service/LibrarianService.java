package com.sahin.lms.account_service.service;

import com.sahin.lms.account_service.mapper.AccountMapper;
import com.sahin.lms.account_service.repository.AccountRepository;
import com.sahin.lms.account_service.util.PasswordUtil;
import com.sahin.lms.infra_aop.annotation.LogExecutionTime;
import com.sahin.lms.infra_authorization.model.AccountLoginType;
import com.sahin.lms.infra_authorization.model.LibraryCard;
import com.sahin.lms.infra_entity.account.jpa.AccountEntity;
import com.sahin.lms.infra_enum.AccountFor;
import com.sahin.lms.infra_enum.LoginType;
import com.sahin.lms.infra_exception.MyRuntimeException;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
import com.sahin.lms.infra_model.account.Librarian;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@LogExecutionTime
@CacheConfig(cacheNames = "accounts")
public class LibrarianService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private LibrarianService self;

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
        entity = accountRepository.save(entity);
        self.cache(entity);
    }

    @Transactional
    public void updateLibrarian(Librarian librarian) {
        if (librarian.getId() == null)
            throw new MyRuntimeException("Librarian to be updated must have an id.", HttpStatus.BAD_REQUEST);

        Optional<AccountEntity> optionalEntity = accountRepository.findById(librarian.getId());

        if (!optionalEntity.isPresent())
            throw new MyRuntimeException("Librarian with id \"" + librarian.getId() + "\" not exist!", HttpStatus.BAD_REQUEST);

        if (!optionalEntity.get().getLibraryCard().getAccountFor().equals(AccountFor.LIBRARIAN))
            throw new MyRuntimeException("The account is not for a librarian", HttpStatus.BAD_REQUEST);

        AccountEntity entity = accountMapper.toEntity(librarian, new CyclePreventiveContext());
        entity.setLibraryCard(optionalEntity.get().getLibraryCard());
        entity = accountRepository.save(entity);
        self.cache(entity);
    }

    @Transactional
    @CacheEvict(cacheNames = {"libraryCards", "accounts"}, key = "#barcode")
    public void deleteLibrarianByBarcode(String barcode) {
        Optional<AccountEntity> optionalEntity = accountRepository.findByLibraryCardBarcode(barcode);

        if (!optionalEntity.isPresent())
            throw new MyRuntimeException("Librarian with card barcode \"" + barcode + "\" not exist!", HttpStatus.BAD_REQUEST);

        if (!optionalEntity.get().getLibraryCard().getAccountFor().equals(AccountFor.LIBRARIAN))
            throw new MyRuntimeException("The account is not for a librarian", HttpStatus.BAD_REQUEST);

        accountRepository.deleteByLibraryCardBarcode(barcode);
    }

    @Transactional
    @Cacheable(key = "#barcode")
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

        List<Librarian> models = accountMapper.toLibrarianModels(entities);

        for (Librarian model : models)
            self.cache(model);

        return models;
    }

    @CachePut(key = "#librarian.libraryCard.barcode")
    public Librarian cache(Librarian librarian) {
        return librarian;
    }

    @CachePut(key = "#entity.libraryCard.barcode")
    public Librarian cache(AccountEntity entity) {
        return accountMapper.toLibrarianModel(entity, new CyclePreventiveContext());
    }

}
