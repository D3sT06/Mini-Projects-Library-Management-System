package com.sahin.library_management.swagger.controller;

import com.sahin.library_management.infra.enums.LogAction;
import com.sahin.library_management.infra.enums.QueryTerm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

@Api(tags = "Log Aggregations")
public interface LogAggregationSwaggerApi {

    @ApiOperation(value = "Get log aggregations by composite key",
            response = Long[].class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Long> getLogAggregationsByCompositeKey(
            @ApiParam(value = "Barcode of the member card", example = "a1111111-1111-1111-1111-111111111111") String barcode,
            @ApiParam(value = "Type of log action", example = "SEARCH_BOOK") LogAction action,
            @ApiParam(value = "Term of query", example = "DAILY") QueryTerm queryTerm);
}
