package com.sahin.library_management.service.member_log;

import com.sahin.library_management.infra.model.log.MemberLog;
import com.sahin.library_management.repository.MemberLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class MemberLogService {

    @Autowired
    private MemberLogRepository memberLogRepository;

    @Transactional
    public void saveAll(Collection<MemberLog> logs) {
        memberLogRepository.saveAll(logs);
    }
}
