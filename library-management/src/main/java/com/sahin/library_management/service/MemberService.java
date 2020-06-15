package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity_model.LibraryCardEntity;
import com.sahin.library_management.infra.entity_model.MemberEntity;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.mapper.MemberMapper;
import com.sahin.library_management.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Transactional
    public void createMember(Member member) {
        if (member.getLibraryCard() != null || member.getId() != null)
            throw new MyRuntimeException("Member to be created cannot have a library card or an id.", HttpStatus.BAD_REQUEST);

        MemberEntity entity = memberMapper.toEntity(member);

        entity.setLibraryCard(new LibraryCardEntity());
        entity.getLibraryCard().setIssuedAt(Instant.now().toEpochMilli());
        entity.getLibraryCard().setActive(true);

        memberRepository.save(entity);
    }

    @Transactional
    public void updateMember(Member member) {
        if (member.getId() == null)
            throw new MyRuntimeException("Member to be created must have an id.", HttpStatus.BAD_REQUEST);

        Optional<MemberEntity> optionalEntity = memberRepository.findById(member.getId());

        if (!optionalEntity.isPresent())
            throw new MyRuntimeException("Member with id \"" + member.getId() + "\" not exist!", HttpStatus.BAD_REQUEST);

        MemberEntity entity = memberMapper.toEntity(member);
        entity.setLibraryCard(optionalEntity.get().getLibraryCard());

        memberRepository.save(entity);
    }

    @Transactional
    public void deleteMemberByBarcode(String barcode) {
        Optional<MemberEntity> optionalEntity = memberRepository.findByLibraryCardBarcode(barcode);

        if (!optionalEntity.isPresent())
            throw new MyRuntimeException("Member with card barcode \"" + barcode + "\" not exist!", HttpStatus.BAD_REQUEST);

        memberRepository.deleteByLibraryCardBarcode(barcode);
    }

    @Transactional
    public Member getMemberByBarcode(String barcode) {
        MemberEntity entity = memberRepository
                .findByLibraryCardBarcode(barcode)
                .orElseThrow(()-> new MyRuntimeException("Member with card barcode " + barcode + " not exist!", HttpStatus.BAD_REQUEST));

        return memberMapper.toModel(entity);
    }
}