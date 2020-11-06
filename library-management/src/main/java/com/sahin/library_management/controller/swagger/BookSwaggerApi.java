package com.sahin.library_management.controller.swagger;

import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.infra.model.book.BookItem;
import com.sahin.library_management.infra.model.search.BookFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Api(tags = "Books")
public interface BookSwaggerApi {

    @ApiOperation(value = "Create a book",
            response = BookItem.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Book> createBook(
            @ApiParam("Book object") Book book);

    @ApiOperation(value = "Update the book",
            response = BookItem.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Book> updateBook(
            @ApiParam("Book object") Book book);

    @ApiOperation(value = "Delete the book by id",
            response = BookItem.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> deleteBookById(
            @ApiParam("Id of the book") Long bookId) ;

    @ApiOperation(value = "Get the book by id",
            response = BookItem.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Book> getBookById(
            @ApiParam("Id of the book") Long bookId) ;

    @ApiOperation(value = "Get all books by filtering with pagination",
            response = BookItem.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Page<Book>> getAll(
            @ApiParam("Book filter object") BookFilter bookFilter,
            @ApiParam("Pageable object") Pageable pageable);

}
