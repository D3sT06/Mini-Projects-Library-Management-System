package com.sahin.lms.library_service.swagger.controller;


import com.sahin.lms.infra.model.book.BookCategory;
import com.sahin.lms.infra.projections.CategoryProjections;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Categories")
public interface BookCategorySwaggerApi {

    @ApiOperation(value = "Create a category",
            response = BookCategory.class,
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "bookCategory",
                    value = "Category object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.CategoryCreateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<BookCategory> createCategory(
            @ApiParam(hidden = true) BookCategory bookCategory);

    @ApiOperation(value = "Update the category",
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "bookCategory",
                    value = "Category object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.CategoryUpdateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Void> updateCategory(
            @ApiParam(hidden = true) BookCategory bookCategory);

    @ApiOperation(value = "Delete the category by id",
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Void> deleteCategoryById(
            @ApiParam(value = "Id of the author", example = "1") Long categoryId);

    @ApiOperation(value = "Get the category by id",
            response = CategoryProjections.CategoryView.class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<CategoryProjections.CategoryView> getCategoryById(
            @ApiParam(value = "Id of the category", example = "1")  Long categoryId);

    @ApiOperation(value = "Get all categories",
            response = CategoryProjections.CategoryView[].class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<List<CategoryProjections.CategoryView>> getAll();

}
