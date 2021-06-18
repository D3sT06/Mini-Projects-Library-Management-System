package com.sahin.lms.library_service.service.member_log;

import com.sahin.lms.infra.enums.LogTopic;
import com.sahin.lms.infra.model.log.MemberLog;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VoidMemberLogPublisherService implements MemberLogPublisherService {

    @Override
    public void send(LogTopic logTopic, MemberLog memberLog) {
        log.error("WARNING: The rabbit queue is not constructed. Change the \"amqp.enabled\" to true.");
    }
}
