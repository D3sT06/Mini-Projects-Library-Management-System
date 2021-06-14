package com.sahin.lms.account_service.swagger.controller;

import com.sahin.lms.infra.model.account.Librarian;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Librarians")
public interface LibrarianSwaggerApi {

    @ApiOperation(value = "Create a librarian",
            response = Librarian.class,
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "librarian",
                    value = "Librarian object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.AccountCreateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Void> createLibrarian(
            @ApiParam(hidden = true) Librarian librarian);

    @ApiOperation(value = "Update the librarian",
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "librarian",
                    value = "Librarian object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.AccountUpdateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Void> updateLibrarian(
            @ApiParam(hidden = true) Librarian librarian);

    @ApiOperation(value = "Delete the librarian by card barcode",
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Void> deleteLibrarianByBarcode(
            @ApiParam("Barcode of the card of the librarian") String barcode);

    @ApiOperation(value = "Get the librarian by card barcode",
            response = Librarian.class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Librarian> getLibrarianByBarcode(
            @ApiParam("Barcode of the card of the librarian") String barcode);

    @ApiOperation(value = "Get all librarians",
            response = Librarian[].class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<List<Librarian>> getAll();
}
