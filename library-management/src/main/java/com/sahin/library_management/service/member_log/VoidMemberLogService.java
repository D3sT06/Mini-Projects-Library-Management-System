package com.sahin.library_management.service.member_log;

import com.sahin.library_management.infra.enums.LogTopic;
import com.sahin.library_management.infra.model.log.MemberLog;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VoidMemberLogService implements MemberLogService {

    @Override
    public void send(LogTopic logTopic, MemberLog memberLog) {
        log.error("WARNING: The rabbit queue is not constructed. Change the \"amqp.enabled\" to true.");
    }
}
