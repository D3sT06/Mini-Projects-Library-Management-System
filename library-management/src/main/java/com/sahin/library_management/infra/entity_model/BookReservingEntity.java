package com.sahin.library_management.infra.entity_model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "book_reserving", schema = "library_management")
@Getter
@Setter
public class BookReservingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_item_id", nullable = false)
    private BookItemEntity bookItem;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity member;

    @Column(name = "reserved_at")
    private Long reservedAt;

    @Column(name = "due_date")
    private Long dueDate;

    @Column(name = "expected_loan_at")
    private Long expectedLoanAt;
}
