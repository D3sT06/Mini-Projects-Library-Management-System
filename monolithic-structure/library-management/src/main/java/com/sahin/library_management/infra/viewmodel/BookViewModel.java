package com.sahin.library_management.infra.viewmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookViewModel {

    private String barcode;
    private String title;
    private String publicationDate;
    private Set<String> categoryIds;
    private String categoryNames;
    private String authorId;
    private String authorFullname;
}
