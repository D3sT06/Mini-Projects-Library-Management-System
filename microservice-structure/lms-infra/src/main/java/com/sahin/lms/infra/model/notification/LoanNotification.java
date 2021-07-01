package com.sahin.lms.infra.model.notification;

import com.sahin.lms.infra.enums.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoanNotification extends BaseNotification implements Serializable {

    private static final long serialVersionUID = 1224354343512300009L;

    private Long bookLoaningId;
    private Long bookLoaningDueDate;

    public LoanNotification(Long id, Long dueDate, NotificationEvent event, String barcode, String email) {
        super(event, barcode, email);
        this.bookLoaningId = id;
        this.bookLoaningDueDate = dueDate;
    }
}
