package com.sahin.library_management.infra.model.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLogAggregation {

    @Id
    private String action;
    private Long actionCount;

}
