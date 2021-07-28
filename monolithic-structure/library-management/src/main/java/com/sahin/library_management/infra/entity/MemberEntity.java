package com.sahin.library_management.infra.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberEntity extends EntityWithUUID {

    private String name;
    private String surname;
    private String email;
    private String phone;
}