package com.sahin.lms.infra.entity.loan.jpa;


import com.sahin.lms.infra.enums.BookStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "book_item_state", schema = "library_management")
@Getter
@Setter
public class BookItemStateEntity {

    @Id
    @Column(name = "item_barcode")
    private String itemBarcode;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookStatus status;
}