package com.sahin.lms.loan_service.swagger.controller;

import com.sahin.lms.infra.model.account.LibraryCard;
import com.sahin.lms.infra.model.book.BookReserving;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "Reservations")
public interface BookItemReserveSwaggerApi {

    @ApiOperation(value = "Reserve the book item by item barcode",
            response = BookReserving.class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<BookReserving> reserveBookItem(
            @ApiIgnore LibraryCard libraryCard,
            @ApiParam(value = "Barcode of the book item", example = "b1111111-1111-1111-1111-111111111111") String bookItemBarcode);

    @ApiOperation(value = "Update the book reservation",
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "bookReserving",
                    value = "Reservation object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.BookReservationUpdateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Void> updateBookReserving(
            @ApiParam(hidden = true, example = "b1111111-1111-1111-1111-111111111111") BookReserving bookReserving);
}
