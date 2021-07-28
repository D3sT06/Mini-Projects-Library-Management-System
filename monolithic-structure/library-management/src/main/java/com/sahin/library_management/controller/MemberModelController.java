package com.sahin.library_management.controller;

import com.sahin.library_management.infra.entity.jpa.AccountEntity;
import com.sahin.library_management.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/model")
public class MemberModelController {

    @Autowired
    private MemberService memberService;


    @GetMapping({"members", "members.html"})
    public String members(Model model) {
        model.addAttribute("members", memberService.getAll());
        return "members";
    }

    @GetMapping("/members/new")
    public String showSignUpForm(AccountEntity member, Model model) {
        model.addAttribute("member", member);
        return "add-member";
    }

    @GetMapping("/members/edit/{barcode}")
    public String showEditForm(@PathVariable String barcode, Model model) {

        AccountEntity member = memberService.getMemberByBarcode(barcode);
        model.addAttribute("member", member);
        return "update-member";
    }

    @PostMapping("/members/create")
    public String createMember(AccountEntity member, Model model) {

        memberService.createMember(member);
        model.addAttribute("members", memberService.getAll());

        return "redirect:/model/members";
    }

    @PostMapping("/members/update/{barcode}")
    public String updateMember(AccountEntity member,@PathVariable String barcode, Model model) {

        member.setId(memberService.getMemberByBarcode(barcode).getId());
        memberService.updateMember(member);
        model.addAttribute("members", memberService.getAll());

        return "redirect:/model/members";
    }

    @GetMapping("/members/delete/{barcode}")
    public String deleteMemberByBarcode(@PathVariable String barcode, Model model) {

        memberService.deleteMemberByBarcode(barcode);
        model.addAttribute("members", memberService.getAll());

        return "redirect:/model/members";
    }
}