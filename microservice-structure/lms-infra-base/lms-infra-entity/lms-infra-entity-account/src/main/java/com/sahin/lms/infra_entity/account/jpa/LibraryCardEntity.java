package com.sahin.lms.infra_entity.account.jpa;

import com.sahin.lms.infra_enum.AccountFor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "library_card", schema = "library_management")
@Getter
@Setter
public class LibraryCardEntity implements Serializable {

    private static final long serialVersionUID = 1905122041950251207L;

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

    @OneToMany(mappedBy = "libraryCard", cascade = CascadeType.ALL)
    private Set<AccountLoginTypeEntity> loginTypes;
}