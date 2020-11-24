package com.sahin.library_management.infra.model.log;

import com.sahin.library_management.infra.enums.LogAction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@ToString
@Document(collection = "member_logs")
public class MemberLog implements Serializable {

    private static final long serialVersionUID = 111345556689102002L;

    @Id
    private String id;
    private String cardBarcode;
    private Long actionTime;
    private LogAction action;
    private String message;
    private String details;
    private HttpStatus httpStatus;

    private MemberLog() {}

    public static class Builder {
        private LogAction action;
        private String message;
        private String details;
        private HttpStatus httpStatus;

        public Builder action(LogAction action, String... params) {
            this.action = action;
            this.message = action.getMessage(params);
            return this;
        }

        public Builder details(String details) {
            this.details = details;
            return this;
        }

        public Builder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public MemberLog build() {
            MemberLog memberLog = new MemberLog();
            memberLog.action = this.action;
            memberLog.message = this.message;
            memberLog.details = this.details;
            memberLog.httpStatus = this.httpStatus;
            memberLog.cardBarcode = SecurityContextHolder.getContext().getAuthentication().getName();
            memberLog.actionTime = Instant.now().toEpochMilli();
            return memberLog;
        }
    }

}
