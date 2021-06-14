package com.sahin.lms.infra.entity.jpa;

import com.sahin.library_management.infra.enums.LoginType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "login_type",
        schema = "library_management",
        uniqueConstraints = @UniqueConstraint(columnNames = {"card_barcode", "type"}))
@Getter
@Setter
public class AccountLoginTypeEntity implements Serializable {

    private static final long serialVersionUID = 1905233152061251207L;

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