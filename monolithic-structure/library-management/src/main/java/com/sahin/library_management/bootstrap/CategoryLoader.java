package com.sahin.library_management.bootstrap;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryLoader implements Loader<BookCategoryEntity> {

    @Autowired
    private LibraryRepository libraryRepository;

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

        libraryRepository.save(category1);
        libraryRepository.save(category2);
        libraryRepository.save(category3);
        libraryRepository.save(category4);
        libraryRepository.save(category5);
    }

    @Override
    public void clearDb() {
        List<BookCategoryEntity> entities = this.getAll();
        entities.forEach(entity -> libraryRepository.deleteById(entity.getBarcode(), BookCategoryEntity.class));
    }

    public List<BookCategoryEntity> getAll() {
        return libraryRepository.findAll(BookCategoryEntity.class)
                .stream()
                .map(entity -> (BookCategoryEntity) entity)
                .collect(Collectors.toList());
    }
}
