package com.sahin.library_management.infra.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.internal.util.stereotypes.Lazy;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "category", schema = "library_management")
@Getter
@Setter
public class BookCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<BookEntity> books;
}