package com.sahin.library_management.swagger.controller;

import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.infra.projections.AuthorProjections;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Authors")
public interface AuthorSwaggerApi {

    @ApiOperation(value = "Create an author",
            response = Author.class,
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "author",
                    value = "Author object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.AuthorCreateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Author> createAuthor(
             @ApiParam(hidden = true) Author author);

    @ApiOperation(value = "Update the author",
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "author",
                    value = "Author object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.AuthorUpdateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Void> updateAuthor(
            @ApiParam(hidden = true) Author author);

    @ApiOperation(value = "Delete the author by id",
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Void> deleteAuthorById(
            @ApiParam(value = "Id of the author", example = "1") Long authorId);

    @ApiOperation(value = "Get the author by id",
            response = AuthorProjections.AuthorView.class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<AuthorProjections.AuthorView> getAuthorById(
            @ApiParam(value = "Id of the author", example = "1") Long authorId);

    @ApiOperation(value = "Get all authors",
            response = AuthorProjections.AuthorView[].class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<List<AuthorProjections.AuthorView>> getAll();
}
