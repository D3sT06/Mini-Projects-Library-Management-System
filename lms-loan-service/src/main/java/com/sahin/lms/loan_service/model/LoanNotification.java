package com.sahin.lms.loan_service.model;

import com.sahin.lms.infra.enums.NotificationEvent;
import com.sahin.lms.infra.model.book.BookLoaning;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class LoanNotification implements Serializable {

    private static final long serialVersionUID = 1224354343512300009L;

    private Long bookLoaningId;
    private NotificationEvent event;
}
