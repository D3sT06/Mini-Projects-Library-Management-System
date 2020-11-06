package com.sahin.library_management.controller.swagger;

import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.book.BookLoaning;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Loans")
public interface BookItemLoanSwaggerApi {

    @ApiOperation(value = "Renew the loaning by item barcode",
            response = BookLoaning.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<BookLoaning> renewBookItem(
            @ApiIgnore LibraryCard libraryCard,
            @ApiParam("Barcode of the book item") String bookItemBarcode);

    @ApiOperation(value = "Check out the book item by item barcode",
            response = BookLoaning.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<BookLoaning> checkOutBookItem(
            @ApiIgnore LibraryCard libraryCard,
            @ApiParam("Barcode of the book item") String bookItemBarcode);

    @ApiOperation(value = "Return the book item by item barcode",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> returnBookItem(
            @ApiIgnore LibraryCard libraryCard,
            @ApiParam("Barcode of the book item") String bookItemBarcode);

    @ApiOperation(value = "Update the book loaning",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> updateBookLoaning(
            @ApiParam("Book loaning object") BookLoaning bookLoaning);
}
