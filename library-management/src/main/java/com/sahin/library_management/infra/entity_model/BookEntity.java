package com.sahin.library_management.infra.entity_model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "book", schema = "library_management")
@Getter
@Setter
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "publication_date")
    private Long publicationDate;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private BookCategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorEntity author;
}
