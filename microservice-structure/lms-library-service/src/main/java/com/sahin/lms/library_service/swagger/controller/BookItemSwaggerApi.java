package com.sahin.lms.library_service.swagger.controller;

import com.sahin.lms.infra.model.book.BookItem;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Book Items")
public interface BookItemSwaggerApi {

    @ApiOperation(value = "Create a book item",
            response = BookItem.class,
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "bookItem",
                    value = "Book item object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.BookItemCreateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<BookItem> createBookItem(
            @ApiParam(hidden = true) BookItem bookItem);

    @ApiOperation(value = "Update the book item",
            response = BookItem.class,
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "bookItem",
                    value = "Book item object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.BookItemUpdateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<BookItem> updateBookItem(
            @ApiParam(hidden = true) BookItem bookItem);

    @ApiOperation(value = "Delete the book item by barcode",
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Void> deleteBookItemByBarcode(
            @ApiParam(value = "Barcode of the book item", example = "b1111111-1111-1111-1111-111111111111") String barcode);

    @ApiOperation(value = "Get the book item by barcode",
            response = BookItem.class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<BookItem> getBookItemByBarcode(
            @ApiParam(value = "Barcode of the book item", example = "b1111111-1111-1111-1111-111111111111") String barcode);

    @ApiOperation(value = "Get book items by book id",
            response = BookItem[].class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<List<BookItem>> getBookItemsByBook(
            @ApiParam(value = "Id of the book", example = "1") Long bookId);
}
