package com.sahin.lms.library_service.swagger.controller;

import com.sahin.lms.infra.model.book.Book;
import com.sahin.lms.infra.model.search.BookFilter;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Api(tags = "Books")
public interface BookSwaggerApi {

    @ApiOperation(value = "Create a book",
            response = Book.class,
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "book",
                    value = "Book object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.BookCreateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Book> createBook(
            @ApiParam(hidden = true) Book book);

    @ApiOperation(value = "Update the book",
            response = Book.class,
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "book",
                    value = "Book object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.BookUpdateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Book> updateBook(
            @ApiParam(hidden = true) Book book);

    @ApiOperation(value = "Delete the book by id",
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Void> deleteBookById(
            @ApiParam(value = "Id of the book", example = "1") Long bookId) ;

    @ApiOperation(value = "Get the book by id",
            response = Book.class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Book> getBookById(
            @ApiParam(value = "Id of the book", example = "1") Long bookId) ;

    @ApiOperation(value = "Get all books by filtering with pagination",
            response = Book[].class,
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "bookFilter",
                    value = "Book filter object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.BookFilterRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Page<Book>> getAll(
            @ApiParam(hidden = true) BookFilter bookFilter,
            @ApiParam("Pageable object") Pageable pageable);

}
