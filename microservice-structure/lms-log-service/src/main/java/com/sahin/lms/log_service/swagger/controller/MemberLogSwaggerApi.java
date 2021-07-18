package com.sahin.lms.log_service.swagger.controller;

import com.sahin.lms.infra_enum.QueryTerm;
import com.sahin.lms.infra_enum.TimeUnit;
import com.sahin.lms.infra_model.log.MemberLogAggregation;
import com.sahin.lms.infra_model.log.MemberLogWithBarcodeAggregation;
import com.sahin.lms.infra_service.member_log.model.MemberLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Member Logs")
public interface MemberLogSwaggerApi {

    @ApiOperation(value = "Get all member logs",
            response = MemberLog[].class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<List<MemberLog>> getAll();

    @ApiOperation(value = "Get member log aggregation results by member",
            response = MemberLogAggregation[].class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<List<MemberLogAggregation>> getActionAggregationsByBarcode(
            @ApiParam(value = "Barcode of the member card", example = "a1111111-1111-1111-1111-111111111111") String barcode,
            @ApiParam(value = "Unit of time", example = "DAYS") TimeUnit unit,
            @ApiParam(value = "Amount of time unit until now", example = "1") Long amount);

    @ApiOperation(value = "Get all member log aggregation results",
            response = MemberLogWithBarcodeAggregation[].class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<List<MemberLogWithBarcodeAggregation>> getAllActionAggregations(
            @ApiParam(value = "Term of query", example = "DAILY") QueryTerm queryTerm);
}
