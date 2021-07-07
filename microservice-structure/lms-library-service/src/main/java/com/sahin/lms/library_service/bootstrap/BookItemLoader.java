package com.sahin.lms.library_service.bootstrap;

import com.sahin.lms.infra.entity.library.jpa.BookEntity;
import com.sahin.lms.infra.entity.library.jpa.BookItemEntity;
import com.sahin.lms.infra.entity.library.jpa.RackEntity;
import com.sahin.lms.infra.enums.BookStatus;
import com.sahin.lms.library_service.repository.BookItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookItemLoader implements Loader<BookItemEntity> {

    @Autowired
    private RackLoader rackLoader;

    @Autowired
    private BookLoader bookLoader;

    @Autowired
    private BookItemRepository bookItemRepository;

    @Override
    public void loadDb() {
        List<BookEntity> books = bookLoader.getAll();
        List<RackEntity> racks = rackLoader.getAll();

        BookItemEntity bookItem1 = new BookItemEntity();
        bookItem1.setBook(books.get(0));
        bookItem1.setRack(racks.get(0));
        bookItem1.setStatus(BookStatus.AVAILABLE);

        BookItemEntity bookItem2 = new BookItemEntity();
        bookItem2.setBook(books.get(0));
        bookItem2.setRack(racks.get(0));
        bookItem2.setStatus(BookStatus.AVAILABLE);

        BookItemEntity bookItem3 = new BookItemEntity();
        bookItem3.setBook(books.get(1));
        bookItem3.setRack(racks.get(0));
        bookItem3.setStatus(BookStatus.AVAILABLE);

        BookItemEntity bookItem4 = new BookItemEntity();
        bookItem4.setBook(books.get(1));
        bookItem4.setRack(racks.get(0));
        bookItem4.setStatus(BookStatus.AVAILABLE);

        BookItemEntity bookItem5 = new BookItemEntity();
        bookItem5.setBook(books.get(2));
        bookItem5.setRack(racks.get(0));
        bookItem5.setStatus(BookStatus.AVAILABLE);

        BookItemEntity bookItem6 = new BookItemEntity();
        bookItem6.setBook(books.get(3));
        bookItem6.setRack(racks.get(0));
        bookItem6.setStatus(BookStatus.AVAILABLE);

        BookItemEntity bookItem7 = new BookItemEntity();
        bookItem7.setBook(books.get(4));
        bookItem7.setRack(racks.get(0));
        bookItem7.setStatus(BookStatus.AVAILABLE);

        BookItemEntity bookItem8 = new BookItemEntity();
        bookItem8.setBook(books.get(4));
        bookItem8.setRack(racks.get(0));
        bookItem8.setStatus(BookStatus.AVAILABLE);

        BookItemEntity bookItem9 = new BookItemEntity();
        bookItem9.setBook(books.get(4));
        bookItem9.setRack(racks.get(0));
        bookItem9.setStatus(BookStatus.AVAILABLE);

        BookItemEntity bookItem10 = new BookItemEntity();
        bookItem10.setBook(books.get(4));
        bookItem10.setRack(racks.get(0));
        bookItem10.setStatus(BookStatus.AVAILABLE);

        bookItemRepository.save(bookItem1);
        bookItemRepository.save(bookItem2);
        bookItemRepository.save(bookItem3);
        bookItemRepository.save(bookItem4);
        bookItemRepository.save(bookItem5);
        bookItemRepository.save(bookItem6);
        bookItemRepository.save(bookItem7);
        bookItemRepository.save(bookItem8);
        bookItemRepository.save(bookItem9);
        bookItemRepository.save(bookItem10);
    }

    @Override
    public void clearDb() {
        bookItemRepository.deleteAll();
    }

    @Override
    public List<BookItemEntity> getAll() {
        return bookItemRepository.findAll();
    }
}
