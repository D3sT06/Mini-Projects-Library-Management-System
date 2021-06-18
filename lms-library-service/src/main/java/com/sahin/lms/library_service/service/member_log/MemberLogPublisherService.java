package com.sahin.lms.library_service.service.member_log;

import com.sahin.lms.infra.enums.LogTopic;
import com.sahin.lms.infra.model.log.MemberLog;
import org.springframework.stereotype.Service;

@Service
public interface MemberLogPublisherService {

    void send(LogTopic logTopic, MemberLog memberLog);

}