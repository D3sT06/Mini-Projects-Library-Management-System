package com.sahin.lms.loan_service.swagger.controller;

import com.sahin.lms.infra_authorization.model.LibraryCard;
import com.sahin.lms.infra_model.loan.BookLoaning;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Loans")
public interface BookItemLoanSwaggerApi {

    @ApiOperation(value = "Renew the loaning by item barcode",
            response = BookLoaning.class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<BookLoaning> renewBookItem(
            @ApiIgnore LibraryCard libraryCard,
            @ApiParam(value = "Barcode of the book item", example = "b1111111-1111-1111-1111-111111111111") String bookItemBarcode);

    @ApiOperation(value = "Check out the book item by item barcode",
            response = BookLoaning.class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<BookLoaning> checkOutBookItem(
            @ApiIgnore LibraryCard libraryCard,
            @ApiParam(value = "Barcode of the book item", example = "b1111111-1111-1111-1111-111111111111") String bookItemBarcode);

    @ApiOperation(value = "Return the book item by item barcode",
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Void> returnBookItem(
            @ApiIgnore LibraryCard libraryCard,
            @ApiParam(value = "Barcode of the book item", example = "b1111111-1111-1111-1111-111111111111") String bookItemBarcode);

    @ApiOperation(value = "Update the book loaning",
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "bookLoaning",
                    value = "Loan object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.BookLoanUpdateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Void> updateBookLoaning(
            @ApiParam(hidden = true) BookLoaning bookLoaning);
}
