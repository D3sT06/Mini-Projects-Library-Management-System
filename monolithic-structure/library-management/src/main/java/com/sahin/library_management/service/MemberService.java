package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.MemberEntity;
import com.sahin.library_management.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private LibraryRepository libraryRepository;

    public void createMember(MemberEntity member) {
        libraryRepository.save(member);
    }

    public void updateMember(MemberEntity member) {
        libraryRepository.update(member);
    }

    public void deleteMemberByBarcode(String barcode) {
        libraryRepository.deleteById(barcode, MemberEntity.class);
    }

    public MemberEntity getMemberByBarcode(String barcode) {
        return (MemberEntity) libraryRepository
                .findById(barcode, MemberEntity.class);
    }

    public List<MemberEntity> getAll() {
        return libraryRepository.findAll(MemberEntity.class)
                .stream()
                .map(entity -> (MemberEntity) entity)
                .collect(Collectors.toList());
    }
}