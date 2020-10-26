package com.sahin.library_management.infra.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Auditable<T> {

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    protected T createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected long createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    protected T lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = false)
    protected long lastModifiedDate;
}
