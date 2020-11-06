package com.sahin.library_management.infra.model.book.swagger;

import com.sahin.library_management.infra.model.book.Author;
import io.swagger.annotations.ApiModelProperty;

public class AuthorRequest extends Author {

    @Override
    @ApiModelProperty(hidden = true)
    public Long getId() {
        return super.getId();
    }
}
