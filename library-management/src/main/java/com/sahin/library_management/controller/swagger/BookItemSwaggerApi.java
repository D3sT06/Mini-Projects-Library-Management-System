package com.sahin.library_management.controller.swagger;

import com.sahin.library_management.infra.model.book.BookItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Book Items")
public interface BookItemSwaggerApi {

    @ApiOperation(value = "Create a book item",
            response = BookItem.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<BookItem> createBookItem(
            @ApiParam("Book item object") BookItem bookItem);

    @ApiOperation(value = "Update the book item",
            response = BookItem.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<BookItem> updateBookItem(
            @ApiParam("Book item object") BookItem bookItem);

    @ApiOperation(value = "Delete the book item by barcode",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> deleteBookItemByBarcode(
            @ApiParam("Barcode of the book item") String barcode);

    @ApiOperation(value = "Get the book item by barcode",
            response = BookItem.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<BookItem> getBookItemByBarcode(
            @ApiParam("Barcode of the book item") String barcode);

    @ApiOperation(value = "Get book items by book id",
            response = BookItem[].class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<List<BookItem>> getBookItemsByBook(
            @ApiParam("Id of the book") Long bookId);
}
