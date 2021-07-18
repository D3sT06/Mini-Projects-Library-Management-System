package com.sahin.lms.loan_service.client;

import com.sahin.lms.infra_model.library.model.BookItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("${feign.client.appName.library}")
public interface LibraryFeignClient {

    @GetMapping("api/book-items/get/{barcode}")
    ResponseEntity<BookItem> getBookItemByBarcode(@RequestHeader(value = "Authorization") String token,
                                                  @PathVariable("barcode") String barcode);
}