package com.sahin.library_management.controller;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.model.log.MemberLog;
import com.sahin.library_management.service.member_log.MemberLogService;
import com.sahin.library_management.swagger.controller.MemberLogSwaggerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/member-logs")
@LogExecutionTime
public class MemberLogController implements MemberLogSwaggerApi {

    @Autowired
    private MemberLogService memberLogService;

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("getAll")
    public ResponseEntity<List<MemberLog>> getAll() {
        return ResponseEntity.ok(memberLogService.getAll());
    }
}
