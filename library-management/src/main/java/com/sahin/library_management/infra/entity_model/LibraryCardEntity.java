package com.sahin.library_management.infra.entity_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahin.library_management.infra.enums.AccountFor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "library_card", schema = "library_management")
@Getter
@Setter
public class LibraryCardEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "barcode")
    private String barcode;

    @Column(name = "issued_at")
    private Long issuedAt;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_for", nullable = false)
    private AccountFor accountFor;
}