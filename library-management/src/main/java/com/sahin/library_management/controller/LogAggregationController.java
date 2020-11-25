package com.sahin.library_management.controller;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.enums.LogAction;
import com.sahin.library_management.infra.enums.QueryTerm;
import com.sahin.library_management.service.LogAggregationService;
import com.sahin.library_management.swagger.controller.LogAggregationSwaggerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/log-aggregation")
@LogExecutionTime
public class LogAggregationController implements LogAggregationSwaggerApi {

    @Autowired
    private LogAggregationService logAggregationService;

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("get-action-aggregations/{barcode}")
    public ResponseEntity<Long> getLogAggregationsByCompositeKey(
            @RequestParam String barcode, @RequestParam LogAction action, @RequestParam QueryTerm queryTerm) {
        return ResponseEntity.ok(logAggregationService.getActionCountsByCompositeKey(barcode, action, queryTerm));
    }
}
