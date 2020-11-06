package com.sahin.library_management.infra.model.book.swagger;

import com.sahin.library_management.infra.model.book.BookCategory;
import io.swagger.annotations.ApiModelProperty;

public class CategoryRequest extends BookCategory {

    @Override
    @ApiModelProperty(hidden = true)
    public Long getId() {
        return super.getId();
    }
}
