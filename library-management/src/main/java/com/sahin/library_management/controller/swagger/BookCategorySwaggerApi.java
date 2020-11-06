package com.sahin.library_management.controller.swagger;


import com.sahin.library_management.infra.model.book.BookCategory;
import com.sahin.library_management.infra.model.book.swagger.CategoryRequest;
import com.sahin.library_management.infra.projections.CategoryProjections;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Categories")
public interface BookCategorySwaggerApi {

    @ApiOperation(value = "Create a category",
            response = BookCategory.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<BookCategory> createCategory(
            @ApiParam("Category object") CategoryRequest bookCategory);

    @ApiOperation(value = "Update the category",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> updateCategory(
            @ApiParam("Category object") BookCategory bookCategory);

    @ApiOperation(value = "Delete the category by id",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> deleteCategoryById(
            @ApiParam("Id of the author") Long categoryId);

    @ApiOperation(value = "Get the category by id",
            response = CategoryProjections.CategoryView.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<CategoryProjections.CategoryView> getCategoryById(
            @ApiParam("Id of the category")  Long categoryId);

    @ApiOperation(value = "Get all categories",
            response = CategoryProjections.CategoryView[].class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<List<CategoryProjections.CategoryView>> getAll();

}
