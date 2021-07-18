package com.sahin.lms.log_service.controller;

import com.sahin.lms.infra_aop.annotation.LogExecutionTime;
import com.sahin.lms.infra_enum.QueryTerm;
import com.sahin.lms.infra_enum.TimeUnit;
import com.sahin.lms.infra_model.log.MemberLogAggregation;
import com.sahin.lms.infra_model.log.MemberLogWithBarcodeAggregation;
import com.sahin.lms.infra_service.member_log.model.MemberLog;
import com.sahin.lms.log_service.service.MemberLogService;
import com.sahin.lms.log_service.swagger.controller.MemberLogSwaggerApi;
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

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("get-action-aggregations")
    public ResponseEntity<List<MemberLogWithBarcodeAggregation>> getAllActionAggregations(
            @RequestParam QueryTerm queryTerm) {
        return ResponseEntity.ok(memberLogService.getActionAggregations(queryTerm));
    }
}
