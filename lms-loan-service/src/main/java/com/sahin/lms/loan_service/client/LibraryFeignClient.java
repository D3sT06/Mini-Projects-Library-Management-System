package com.sahin.lms.loan_service.client;

import com.sahin.lms.infra.model.book.BookItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("${feign.client.appName.library}")
public interface LibraryFeignClient {

    @PutMapping("api/book-items/update")
    ResponseEntity<BookItem> updateBookItem(@RequestBody BookItem bookItem);

    @GetMapping("api/book-items/get/{barcode}")
    ResponseEntity<BookItem> getBookItemByBarcode(@PathVariable String barcode);
}
