package com.sahin.library_management.controller.swagger;

import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.book.BookReserving;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Reservations")
public interface BookItemReserveSwaggerApi {

    @ApiOperation(value = "Reserve the book item by item barcode",
            response = BookReserving.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<BookReserving> reserveBookItem(
            @ApiIgnore LibraryCard libraryCard,
            @ApiParam("Barcode of the book item") String bookItemBarcode);

    @ApiOperation(value = "Update the book reservation",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> updateBookReserving(
            @ApiParam("Book reserving object") BookReserving bookReserving);
}
