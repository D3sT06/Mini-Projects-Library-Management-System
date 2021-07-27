package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.AccountEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.enums.LoginType;
import com.sahin.library_management.infra.model.account.AccountLoginType;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.account.Member;
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
public class MemberService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Transactional
    public void createMember(Member member) {
        String password = PasswordUtil.createRandomPassword();

        LibraryCard card = new LibraryCard();
        card.setIssuedAt(Instant.now().toEpochMilli());
        card.setActive(true);
        card.setAccountFor(AccountFor.MEMBER);
        card.setPassword(password);

        AccountLoginType passwordType = new AccountLoginType();
        passwordType.setType(LoginType.PASSWORD);
        passwordType.setLibraryCard(card);

        card.setLoginTypes(new HashSet<>(Arrays.asList(passwordType)));
        member.setLibraryCard(card);

        AccountEntity entity = accountMapper.toEntity(member, new CyclePreventiveContext());
        accountRepository.save(entity);
    }

    @Transactional
    public void updateMember(Member member) {
        Optional<AccountEntity> optionalEntity = accountRepository.findById(member.getId());
        AccountEntity entity = accountMapper.toEntity(member, new CyclePreventiveContext());
        entity.setLibraryCard(optionalEntity.get().getLibraryCard());
        entity.setType(AccountFor.MEMBER);
        accountRepository.save(entity);
    }

    @Transactional
    public void deleteMemberByBarcode(String barcode) {
        accountRepository.deleteByLibraryCardBarcode(barcode);
    }

    @Transactional
    public Member getMemberByBarcode(String barcode) {
        AccountEntity entity = accountRepository
                .findByLibraryCardBarcode(barcode)
                .get();

        return (Member) accountMapper.toModel(entity);
    }

    public List<Member> getAll() {
        List<AccountEntity> entities = accountRepository
                .findAll();

        List<AccountEntity> filteredEntities = entities.stream()
                .filter(accountEntity -> accountEntity.getLibraryCard().getAccountFor().equals(AccountFor.MEMBER))
                .collect(Collectors.toList());

        return accountMapper.toMemberModels(filteredEntities);
    }
}