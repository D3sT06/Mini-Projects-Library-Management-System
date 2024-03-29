package com.sahin.lms.infra_entity.library.jpa;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import javax.persistence.*;


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