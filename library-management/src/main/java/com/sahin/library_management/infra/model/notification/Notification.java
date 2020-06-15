package com.sahin.library_management.infra.model.notification;

import com.sahin.library_management.infra.enums.NotificationAbout;
import com.sahin.library_management.infra.model.account.Member;
import com.sahin.library_management.infra.model.book.BookItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notification {
    private Long id;
    private Member member;
    private BookItem bookItem;
    private NotificationAbout about;
}
