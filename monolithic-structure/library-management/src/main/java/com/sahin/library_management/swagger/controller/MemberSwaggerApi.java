package com.sahin.library_management.swagger.controller;

import com.sahin.library_management.infra.model.account.Member;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Members")
public interface MemberSwaggerApi {

    @ApiOperation(value = "Create a member",
            response = Member.class,
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "member",
                    value = "Member object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.AccountCreateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Void> createMember(
            @ApiParam("Member object") Member member);

    @ApiOperation(value = "Update the member",
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "member",
                    value = "Member object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.AccountUpdateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Void> updateMember(
            @ApiParam("Member object") Member member);

    @ApiOperation(value = "Delete the member by card barcode",
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Void> deleteMemberByBarcode(
            @ApiParam("Barcode of the card of the member") String barcode);

    @ApiOperation(value = "Get the member by card barcode",
            response = Member.class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Member> getMemberByBarcode(
            @ApiParam("Barcode of the card of the member") String barcode);

    @ApiOperation(value = "Get all members",
            response = Member[].class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<List<Member>> getAll();
}
