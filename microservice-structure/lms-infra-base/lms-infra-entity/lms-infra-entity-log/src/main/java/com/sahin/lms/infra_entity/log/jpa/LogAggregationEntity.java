package com.sahin.lms.infra_entity.log.jpa;

import com.sahin.lms.infra_entity.log.model.LogAggregationCompositeKey;
import com.sahin.lms.infra_enum.AccountFor;
import com.sahin.lms.infra_enum.LogAction;
import com.sahin.lms.infra_enum.QueryTerm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Entity
@Table(name = "log_aggregation",
        schema = "library_management")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@IdClass(LogAggregationCompositeKey.class)
public class LogAggregationEntity {

    @Id
    @Column(name = "barcode")
    private String barcode;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected long createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = false)
    protected long lastModifiedDate;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "log_action", nullable = false)
    private LogAction action;

    @Column(name = "action_count", nullable = false)
    private Long actionCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_for", nullable = false)
    private AccountFor accountFor;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "query_term", nullable = false)
    private QueryTerm queryTerm;
}