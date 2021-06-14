package com.sahin.lms.infra.entity.jpa;

import lombok.Getter;
import lombok.Setter;

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