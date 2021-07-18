package com.sahin.lms.infra_entity.log.document;

import com.sahin.lms.infra_enum.LogAction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;

import javax.persistence.Id;

@Getter
@Setter
@ToString
@Document(collection = "member_logs")
public class MemberLogEntity {

    @Id
    private String id;
    private String cardBarcode;
    private Long actionTime;
    private LogAction action;
    private String message;
    private String details;
    private HttpStatus httpStatus;
}
