package com.sahin.lms.loan_service.model;

import com.sahin.lms.infra.enums.NotificationEvent;
import com.sahin.lms.infra.model.book.BookLoaning;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoanNotification {
    private BookLoaning bookLoaning;
    private NotificationEvent event;
}
