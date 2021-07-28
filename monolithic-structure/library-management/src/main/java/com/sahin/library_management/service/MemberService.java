package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.AccountEntity;
import com.sahin.library_management.infra.entity.AccountLoginTypeEntity;
import com.sahin.library_management.infra.entity.LibraryCardEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.enums.LoginType;
import com.sahin.library_management.repository.LibraryRepository;
import com.sahin.library_management.repository.jpa.jpa.AccountRepository;
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
    private LibraryRepository libraryRepository;

    public void createMember(AccountEntity member) {
        String password = PasswordUtil.createRandomPassword();

        LibraryCardEntity card = new LibraryCardEntity();
        card.setIssuedAt(Instant.now().toEpochMilli());
        card.setActive(true);
        card.setAccountFor(AccountFor.MEMBER);
        card.setPassword(password);

        AccountLoginTypeEntity passwordType = new AccountLoginTypeEntity();
        passwordType.setType(LoginType.PASSWORD);
        passwordType.setLibraryCard(card);

        card.setLoginTypes(new HashSet<>(Arrays.asList(passwordType)));
        member.setLibraryCard(card);

        accountRepository.save(member);
    }

    public void updateMember(AccountEntity member) {
        Optional<AccountEntity> optionalEntity = accountRepository.findById(member.getId());
        member.setLibraryCard(optionalEntity.get().getLibraryCard());
        member.setType(AccountFor.MEMBER);
        accountRepository.save(member);
    }

    public void deleteMemberByBarcode(String barcode) {
        accountRepository.deleteByLibraryCardBarcode(barcode);
    }

    public AccountEntity getMemberByBarcode(String barcode) {
        return accountRepository
                .findByLibraryCardBarcode(barcode)
                .get();
    }

    public List<AccountEntity> getAll() {
        List<AccountEntity> entities = accountRepository
                .findAll();

        return entities.stream()
                .filter(accountEntity -> accountEntity.getLibraryCard().getAccountFor().equals(AccountFor.MEMBER))
                .collect(Collectors.toList());
    }
}