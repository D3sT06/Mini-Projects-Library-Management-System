package com.sahin.lms.loan_service.client;

import com.sahin.lms.infra.model.account.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("${feign.client.appName.account}")
public interface AccountFeignClient {

    @GetMapping("api/members/get/{barcode}")
    ResponseEntity<Member> getMemberByBarcode(@PathVariable String barcode);
}
