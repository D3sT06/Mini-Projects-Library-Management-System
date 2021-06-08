package com.sahin.library_management.service;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.entity.jpa.AccountEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.enums.LoginType;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.AccountLoginType;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.mapper.AccountMapper;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.repository.jpa.AccountRepository;
import com.sahin.library_management.util.PasswordUtil;
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
public class MemberService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private MemberService self;

    @Transactional
    public void createMember(Member member) {
        if (member.getLibraryCard() != null || member.getId() != null)
            throw new MyRuntimeException("Member to be created cannot have a library card or an id.", HttpStatus.BAD_REQUEST);

        String password = PasswordUtil.createRandomPassword();

        LibraryCard card = new LibraryCard();
        card.setIssuedAt(Instant.now().toEpochMilli());
        card.setActive(true);
        card.setAccountFor(AccountFor.MEMBER);
        card.setPassword(passwordEncoder.encode(password));

        AccountLoginType passwordType = new AccountLoginType();
        passwordType.setType(LoginType.PASSWORD);
        passwordType.setLibraryCard(card);

        card.setLoginTypes(new HashSet<>(Arrays.asList(passwordType)));
        member.setLibraryCard(card);

        AccountEntity entity = accountMapper.toEntity(member, new CyclePreventiveContext());
        entity = accountRepository.save(entity);
        self.cache(entity);
    }

    @Transactional
    public void updateMember(Member member) {
        if (member.getId() == null)
            throw new MyRuntimeException("Member to be created must have an id.", HttpStatus.BAD_REQUEST);

        Optional<AccountEntity> optionalEntity = accountRepository.findById(member.getId());

        if (!optionalEntity.isPresent())
            throw new MyRuntimeException("Member with id \"" + member.getId() + "\" not exist!", HttpStatus.BAD_REQUEST);

        if (!optionalEntity.get().getLibraryCard().getAccountFor().equals(AccountFor.MEMBER))
            throw new MyRuntimeException("The account is not for a member", HttpStatus.BAD_REQUEST);

        AccountEntity entity = accountMapper.toEntity(member, new CyclePreventiveContext());
        entity.setLibraryCard(optionalEntity.get().getLibraryCard());
        entity.setType(AccountFor.MEMBER);
        entity = accountRepository.save(entity);
        self.cache(entity);
    }

    @Transactional
    @CacheEvict(cacheNames = {"libraryCards", "accounts"}, key = "#barcode")
    public void deleteMemberByBarcode(String barcode) {
        Optional<AccountEntity> optionalEntity = accountRepository.findByLibraryCardBarcode(barcode);

        if (!optionalEntity.isPresent())
            throw new MyRuntimeException("Member with card barcode \"" + barcode + "\" not exist!", HttpStatus.BAD_REQUEST);

        if (!optionalEntity.get().getLibraryCard().getAccountFor().equals(AccountFor.MEMBER))
            throw new MyRuntimeException("The account is not for a member", HttpStatus.BAD_REQUEST);

        accountRepository.deleteByLibraryCardBarcode(barcode);
    }

    @Transactional
    @Cacheable(key = "#barcode")
    public Member getMemberByBarcode(String barcode) {
        AccountEntity entity = accountRepository
                .findByLibraryCardBarcode(barcode)
                .orElseThrow(()-> new MyRuntimeException("Member with card barcode " + barcode + " not exist!", HttpStatus.BAD_REQUEST));

        if (!(accountMapper.toModel(entity) instanceof Member))
            throw new MyRuntimeException("The account is not for a member", HttpStatus.BAD_REQUEST);

        return (Member) accountMapper.toModel(entity);
    }

    public List<Member> getAll() {
        List<AccountEntity> entities = accountRepository
                .getAll(AccountFor.MEMBER);

        List<Member> models = accountMapper.toMemberModels(entities);

        for (Member model : models)
            self.cache(model);

        return models;
    }

    @CachePut(key = "#member.libraryCard.barcode")
    public Member cache(Member member) {
        return member;
    }

    @CachePut(key = "#entity.libraryCard.barcode")
    public Member cache(AccountEntity entity) {
        return accountMapper.toMemberModel(entity, new CyclePreventiveContext());
    }

}