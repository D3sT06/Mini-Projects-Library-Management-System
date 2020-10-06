package com.sahin.library_management.controller;

import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("create")
    public ResponseEntity<Void> createMember(@RequestBody Member member) {
        memberService.createMember(member);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PutMapping("update")
    public ResponseEntity<Void> updateMember(@RequestBody Member member) {
        memberService.updateMember(member);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping("delete/{barcode}")
    public ResponseEntity<Void> deleteMemberByBarcode(@PathVariable String barcode) {
        memberService.deleteMemberByBarcode(barcode);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("get/{barcode}")
    public ResponseEntity<Member> getMemberByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(memberService.getMemberByBarcode(barcode));
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @GetMapping("getAll")
    public ResponseEntity<List<Member>> getAll() {
        return ResponseEntity.ok(memberService.getAll());
    }
}
