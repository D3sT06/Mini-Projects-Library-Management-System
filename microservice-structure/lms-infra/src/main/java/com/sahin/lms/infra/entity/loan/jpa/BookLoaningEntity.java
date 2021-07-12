package com.sahin.lms.infra.entity.loan.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "book_loaning", schema = "library_management")
@Getter
@Setter
public class BookLoaningEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_item_state_id", nullable = false)
    private BookItemStateEntity bookItemState;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "loaned_at")
    private Long loanedAt;

    @Column(name = "due_date")
    private Long dueDate;

    @Column(name = "returned_at")
    private Long returnedAt;
}