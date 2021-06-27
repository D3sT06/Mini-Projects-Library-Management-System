package com.sahin.library_management.service.member_log;

import com.sahin.library_management.infra.enums.LogTopic;
import com.sahin.library_management.infra.model.log.MemberLog;
import org.springframework.stereotype.Service;

@Service
public interface MemberLogPublisherService {

    void send(LogTopic logTopic, MemberLog memberLog);

}