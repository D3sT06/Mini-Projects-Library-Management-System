package com.sahin.library_management.controller;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.enums.TimeUnit;
import com.sahin.library_management.infra.model.log.MemberLog;
import com.sahin.library_management.infra.model.log.MemberLogAggregation;
import com.sahin.library_management.service.member_log.MemberLogService;
import com.sahin.library_management.swagger.controller.MemberLogSwaggerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("get-action-aggregations/{barcode}")
    public ResponseEntity<List<MemberLogAggregation>> getActionAggregationsByBarcode(
            @PathVariable String barcode, @RequestParam TimeUnit unit, @RequestParam Long amount) {
        return ResponseEntity.ok(memberLogService.getActionAggregationsByBarcode(barcode, unit, amount));
    }
}
