package com.sahin.library_management.restcontroller;

import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("create")
    public ResponseEntity<Void> createMember(@RequestBody Member member) {
        memberService.createMember(member);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update")
    public ResponseEntity<Void> updateMember(@RequestBody Member member) {
        memberService.updateMember(member);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{barcode}")
    public ResponseEntity<Void> deleteMemberByBarcode(@PathVariable String barcode) {
        memberService.deleteMemberByBarcode(barcode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get/{barcode}")
    public ResponseEntity<Member> getMemberByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(memberService.getMemberByBarcode(barcode));
    }

    @GetMapping("getAll")
    public ResponseEntity<List<Member>> getAll() {
        return ResponseEntity.ok(memberService.getAll());
    }
}
