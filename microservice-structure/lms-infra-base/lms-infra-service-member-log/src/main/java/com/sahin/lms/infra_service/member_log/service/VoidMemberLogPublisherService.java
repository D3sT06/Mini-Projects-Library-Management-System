package com.sahin.lms.infra_service.member_log.service;

import com.sahin.lms.infra_enum.LogTopic;
import com.sahin.lms.infra_service.member_log.model.MemberLog;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VoidMemberLogPublisherService implements MemberLogPublisherService {

    @Override
    public void send(LogTopic logTopic, MemberLog memberLog) {
        log.error("WARNING: The rabbit queue is not constructed. Change the \"amqp.enabled\" to true.");
    }
}
