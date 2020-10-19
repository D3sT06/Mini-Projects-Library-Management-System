package com.sahin.library_management.bootstrap;

import com.sahin.library_management.infra.entity_model.BookCategoryEntity;
import com.sahin.library_management.repository.BookCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryLoader implements Loader<BookCategoryEntity> {

    @Autowired
    private BookCategoryRepository categoryRepository;

    @Override
    public void loadDb() {

        BookCategoryEntity category1 = new BookCategoryEntity();
        category1.setName("Edebiyat");

        BookCategoryEntity category2 = new BookCategoryEntity();
        category2.setName("Çocuk ve Gençlik");

        BookCategoryEntity category3 = new BookCategoryEntity();
        category3.setName("Araştırma / Tarih");

        BookCategoryEntity category4 = new BookCategoryEntity();
        category4.setName("Felsefe");

        BookCategoryEntity category5 = new BookCategoryEntity();
        category5.setName("Bilim");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);
        categoryRepository.save(category5);
    }

    @Override
    public void clearDb() {
        categoryRepository.deleteAll();
    }

    @Override
    public List<BookCategoryEntity> getAll() {
        return categoryRepository.findAll();
    }
}
