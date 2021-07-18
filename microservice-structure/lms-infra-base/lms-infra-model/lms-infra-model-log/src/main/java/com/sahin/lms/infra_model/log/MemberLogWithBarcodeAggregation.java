package com.sahin.lms.infra_model.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLogWithBarcodeAggregation {

    @Id
    private Group group;
    private Long actionCount;

    @Getter
    @Setter
    public class Group {
        private String action;
        private String cardBarcode;
    }
}
