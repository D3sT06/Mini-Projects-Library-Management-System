package com.sahin.library_management.controller.swagger;

import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.infra.model.book.swagger.AuthorRequest;
import com.sahin.library_management.infra.projections.AuthorProjections;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Authors")
public interface AuthorSwaggerApi {

    @ApiOperation(value = "Create an author",
            response = Author.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Author> createAuthor(
             @ApiParam("Author object") AuthorRequest author);

    @ApiOperation(value = "Update the author",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> updateAuthor(
            @ApiParam("Author object") Author author);

    @ApiOperation(value = "Delete the author by id",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> deleteAuthorById(
            @ApiParam("Id of the author") Long authorId);

    @ApiOperation(value = "Get the author by id",
            response = AuthorProjections.AuthorView.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<AuthorProjections.AuthorView> getAuthorById(
            @ApiParam("Id of the author") Long authorId);

    @ApiOperation(value = "Get all authors",
            response = AuthorProjections.AuthorView[].class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<List<AuthorProjections.AuthorView>> getAll();
}
