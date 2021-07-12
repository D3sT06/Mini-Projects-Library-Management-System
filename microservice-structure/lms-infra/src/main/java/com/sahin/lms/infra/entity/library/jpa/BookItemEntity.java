package com.sahin.lms.infra.entity.library.jpa;

import com.sahin.lms.infra.enums.BookStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "book_item", schema = "library_management")
@Getter
@Setter
public class BookItemEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "barcode")
    private String barcode;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "rack_id", nullable = false)
    private RackEntity rack;
}