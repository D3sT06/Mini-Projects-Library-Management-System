package com.sahin.lms.account_service.controller;

import com.sahin.lms.account_service.service.MemberService;
import com.sahin.lms.account_service.swagger.controller.MemberSwaggerApi;
import com.sahin.lms.infra.annotation.LogExecutionTime;
import com.sahin.lms.infra.model.account.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/members")
@LogExecutionTime
public class MemberController implements MemberSwaggerApi {

    @Autowired
    private MemberService memberService;

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("create")
    public ResponseEntity<Void> createMember(@RequestBody @Valid Member member) {
        memberService.createMember(member);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<Void> updateMember(@RequestBody @Valid Member member) {
        memberService.updateMember(member);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @DeleteMapping("delete/{barcode}")
    public ResponseEntity<Void> deleteMemberByBarcode(@PathVariable String barcode) {
        memberService.deleteMemberByBarcode(barcode);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_LIBRARIAN')")
    @GetMapping("get/{barcode}")
    public ResponseEntity<Member> getMemberByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(memberService.getMemberByBarcode(barcode));
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("getAll")
    public ResponseEntity<List<Member>> getAll() {
        return ResponseEntity.ok(memberService.getAll());
    }
}
