package com.sahin.lms.infra_entity.library.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rack", schema = "library_management")
@Getter
@Setter
public class RackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "location", nullable = false)
    private String location;
}