package com.sahin.lms.infra.entity.account.jpa;

import com.sahin.lms.infra.entity.common.Auditable;
import com.sahin.lms.infra.enums.AccountFor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "account", schema = "library_management")
@Getter
@Setter
public class AccountEntity extends Auditable<String> implements Serializable {

    private static final long serialVersionUID = 2214233152061235312L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "library_card_id", nullable = false)
    private LibraryCardEntity libraryCard;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AccountFor type;
}