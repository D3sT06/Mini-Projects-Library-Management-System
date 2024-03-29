package com.sahin.lms.infra_entity.library.jpa;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "author", schema = "library_management")
@Getter
@Setter
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @OneToMany(mappedBy = "author")
    private List<BookEntity> books;
}
