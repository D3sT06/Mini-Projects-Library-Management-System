package com.sahin.library_management.controller.swagger;

import com.sahin.library_management.infra.model.account.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Members")
public interface MemberSwaggerApi {

    @ApiOperation(value = "Create a member",
            response = Member.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> createMember(
            @ApiParam("Member object") Member member);

    @ApiOperation(value = "Update the member",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> updateMember(
            @ApiParam("Member object") Member member);

    @ApiOperation(value = "Delete the member by card barcode",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> deleteMemberByBarcode(
            @ApiParam("Barcode of the card of the member") String barcode);

    @ApiOperation(value = "Get the member by card barcode",
            response = Member.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Member> getMemberByBarcode(
            @ApiParam("Barcode of the card of the member") String barcode);

    @ApiOperation(value = "Get all members",
            response = Member[].class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<List<Member>> getAll();
}
