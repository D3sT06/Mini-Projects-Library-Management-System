package com.sahin.library_management.infra.entity;

import com.sahin.library_management.infra.enums.LoginType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "login_type",
        schema = "library_management",
        uniqueConstraints = @UniqueConstraint(columnNames = {"card_barcode", "type"}))
@Getter
@Setter
public class AccountLoginTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_barcode", nullable = false)
    private LibraryCardEntity libraryCard;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LoginType type;

    @Column(name = "type_specific_key")
    private String typeSpecificKey;
}