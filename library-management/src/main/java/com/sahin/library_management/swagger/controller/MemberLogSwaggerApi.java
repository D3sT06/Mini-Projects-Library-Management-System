package com.sahin.library_management.swagger.controller;

import com.sahin.library_management.infra.model.log.MemberLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Member Logs")
public interface MemberLogSwaggerApi {

    @ApiOperation(value = "Get all member logs",
            response = MemberLog[].class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<List<MemberLog>> getAll();
}
