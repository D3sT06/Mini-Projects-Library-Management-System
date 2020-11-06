package com.sahin.library_management.controller.swagger;

import com.sahin.library_management.infra.model.account.Librarian;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Librarians")
public interface LibrarianSwaggerApi {

    @ApiOperation(value = "Create a librarian",
            response = Librarian.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> createLibrarian(
            @ApiParam("Librarian object") Librarian librarian);

    @ApiOperation(value = "Update the librarian",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> updateLibrarian(
            @ApiParam("Librarian object") Librarian librarian);

    @ApiOperation(value = "Delete the librarian by card barcode",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> deleteLibrarianByBarcode(
            @ApiParam("Barcode of the card of the librarian") String barcode);

    @ApiOperation(value = "Get the librarian by card barcode",
            response = Librarian.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Librarian> getLibrarianByBarcode(
            @ApiParam("Barcode of the card of the librarian") String barcode);

    @ApiOperation(value = "Get all librarians",
            response = Librarian[].class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<List<Librarian>> getAll();
}
