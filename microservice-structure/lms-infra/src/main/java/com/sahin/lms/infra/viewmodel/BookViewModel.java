package com.sahin.lms.infra.viewmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookViewModel {

    private Long id;
    private String title;
    private String publicationDate;
    private Set<Long> categoryIds;
    private String categoryNames;
    private Long authorId;
    private String authorFullname;
}
