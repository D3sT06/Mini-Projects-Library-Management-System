package com.sahin.lms.infra_service.member_log.service;

import com.sahin.lms.infra_enum.LogTopic;
import com.sahin.lms.infra_service.member_log.model.MemberLog;
import org.springframework.stereotype.Service;

@Service
public interface MemberLogPublisherService {

    void send(LogTopic logTopic, MemberLog memberLog);

}