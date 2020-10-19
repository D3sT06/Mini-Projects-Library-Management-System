package com.sahin.library_management.bootstrap;

import com.sahin.library_management.infra.entity_model.*;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.enums.BookStatus;
import com.sahin.library_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;

@Component
@Profile("dev")
public class H2Loader implements CommandLineRunner {

    @Autowired
    private LibraryCardRepository libraryCardRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookItemRepository bookItemRepository;

    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    @Autowired
    private RackRepository rackRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
          loadAccounts();
          loadBooks();
    }


    private void loadBooks() {
        if (bookItemRepository.count() == 0) {

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

            AuthorEntity author1 = new AuthorEntity();
            author1.setName("Jose");
            author1.setSurname("Saramago");

            AuthorEntity author2 = new AuthorEntity();
            author2.setName("John");
            author2.setSurname("Steinbeck");

            AuthorEntity author3 = new AuthorEntity();
            author3.setName("George");
            author3.setSurname("Orwell");

            AuthorEntity author4 = new AuthorEntity();
            author4.setName("Stephen");
            author4.setSurname("Hawking");

            BookEntity book1 = new BookEntity();
            book1.setAuthor(author1);
            book1.setTitle("Körlük");
            book1.setCategory(category1);
            book1.setPublicationDate(LocalDate.of(1990, 10, 1));

            BookEntity book2 = new BookEntity();
            book2.setAuthor(author2);
            book2.setTitle("Fareler ve İnsanlar");
            book2.setCategory(category1);
            book2.setPublicationDate(LocalDate.of(1990, 10, 1));

            BookEntity book3 = new BookEntity();
            book3.setAuthor(author3);
            book3.setTitle("Hayvan Çiftliği");
            book3.setCategory(category1);
            book3.setPublicationDate(LocalDate.of(1995, 9, 1));

            BookEntity book4 = new BookEntity();
            book4.setAuthor(author3);
            book4.setTitle("1984");
            book4.setCategory(category1);
            book4.setPublicationDate(LocalDate.of(1985, 3, 1));

            BookEntity book5 = new BookEntity();
            book5.setAuthor(author4);
            book5.setTitle("Zamanın Kısa Tarihi");
            book5.setCategory(category5);
            book5.setPublicationDate(LocalDate.of(2010, 7, 1));


            RackEntity rack1 = new RackEntity();
            rack1.setLocation("A-1");

            BookItemEntity bookItem1 = new BookItemEntity();
            bookItem1.setBook(book1);
            bookItem1.setRack(rack1);
            bookItem1.setStatus(BookStatus.AVAILABLE);

            BookItemEntity bookItem2 = new BookItemEntity();
            bookItem2.setBook(book1);
            bookItem2.setRack(rack1);
            bookItem2.setStatus(BookStatus.AVAILABLE);

            BookItemEntity bookItem3 = new BookItemEntity();
            bookItem3.setBook(book2);
            bookItem3.setRack(rack1);
            bookItem3.setStatus(BookStatus.AVAILABLE);

            BookItemEntity bookItem4 = new BookItemEntity();
            bookItem4.setBook(book2);
            bookItem4.setRack(rack1);
            bookItem4.setStatus(BookStatus.AVAILABLE);

            BookItemEntity bookItem5 = new BookItemEntity();
            bookItem5.setBook(book3);
            bookItem5.setRack(rack1);
            bookItem5.setStatus(BookStatus.AVAILABLE);

            BookItemEntity bookItem6 = new BookItemEntity();
            bookItem6.setBook(book4);
            bookItem6.setRack(rack1);
            bookItem6.setStatus(BookStatus.AVAILABLE);

            BookItemEntity bookItem7 = new BookItemEntity();
            bookItem7.setBook(book5);
            bookItem7.setRack(rack1);
            bookItem7.setStatus(BookStatus.AVAILABLE);

            BookItemEntity bookItem8 = new BookItemEntity();
            bookItem8.setBook(book5);
            bookItem8.setRack(rack1);
            bookItem8.setStatus(BookStatus.AVAILABLE);

            BookItemEntity bookItem9 = new BookItemEntity();
            bookItem9.setBook(book5);
            bookItem9.setRack(rack1);
            bookItem9.setStatus(BookStatus.AVAILABLE);

            BookItemEntity bookItem10 = new BookItemEntity();
            bookItem10.setBook(book5);
            bookItem10.setRack(rack1);
            bookItem10.setStatus(BookStatus.AVAILABLE);

            rackRepository.save(rack1);

            bookCategoryRepository.save(category1);
            bookCategoryRepository.save(category2);
            bookCategoryRepository.save(category3);
            bookCategoryRepository.save(category4);
            bookCategoryRepository.save(category5);

            authorRepository.save(author1);
            authorRepository.save(author2);
            authorRepository.save(author3);
            authorRepository.save(author4);

            bookRepository.save(book1);
            bookRepository.save(book2);
            bookRepository.save(book3);
            bookRepository.save(book4);
            bookRepository.save(book5);

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
    }

    private void loadAccounts() {
        if (libraryCardRepository.count() == 0) {
            LibraryCardEntity libraryCardEntity1 = new LibraryCardEntity();
            libraryCardEntity1.setAccountFor(AccountFor.LIBRARIAN);
            libraryCardEntity1.setActive(true);
            libraryCardEntity1.setIssuedAt(Instant.now().getEpochSecond());
            libraryCardEntity1.setPassword(passwordEncoder.encode("1234"));

            LibraryCardEntity libraryCardEntity2 = new LibraryCardEntity();
            libraryCardEntity2.setAccountFor(AccountFor.LIBRARIAN);
            libraryCardEntity2.setActive(true);
            libraryCardEntity2.setIssuedAt(Instant.now().getEpochSecond());
            libraryCardEntity2.setPassword(passwordEncoder.encode("1234"));


            LibraryCardEntity libraryCardEntity3 = new LibraryCardEntity();
            libraryCardEntity3.setAccountFor(AccountFor.LIBRARIAN);
            libraryCardEntity3.setActive(true);
            libraryCardEntity3.setIssuedAt(Instant.now().getEpochSecond());
            libraryCardEntity3.setPassword(passwordEncoder.encode("1234"));


            LibraryCardEntity libraryCardEntity4 = new LibraryCardEntity();
            libraryCardEntity4.setAccountFor(AccountFor.MEMBER);
            libraryCardEntity4.setActive(true);
            libraryCardEntity4.setIssuedAt(Instant.now().getEpochSecond());
            libraryCardEntity4.setPassword(passwordEncoder.encode("1234"));


            LibraryCardEntity libraryCardEntity5 = new LibraryCardEntity();
            libraryCardEntity5.setAccountFor(AccountFor.MEMBER);
            libraryCardEntity5.setActive(true);
            libraryCardEntity5.setIssuedAt(Instant.now().getEpochSecond());
            libraryCardEntity5.setPassword(passwordEncoder.encode("1234"));


            LibrarianEntity librarianEntity1 = new LibrarianEntity();
            librarianEntity1.setName("Serkan");
            librarianEntity1.setSurname("Sahin");
            librarianEntity1.setEmail("serkans@sahin.com");
            librarianEntity1.setLibraryCard(libraryCardEntity1);

            LibrarianEntity librarianEntity2 = new LibrarianEntity();
            librarianEntity2.setName("John");
            librarianEntity2.setSurname("Doe");
            librarianEntity2.setEmail("johndoe@sahin.com");
            librarianEntity2.setLibraryCard(libraryCardEntity2);

            LibrarianEntity librarianEntity3 = new LibrarianEntity();
            librarianEntity3.setName("Alex");
            librarianEntity3.setSurname("Watt");
            librarianEntity3.setEmail("alexw@sahin.com");
            librarianEntity3.setLibraryCard(libraryCardEntity3);

            MemberEntity memberEntity1 = new MemberEntity();
            memberEntity1.setName("Wall");
            memberEntity1.setSurname("Orgh");
            memberEntity1.setEmail("wallo@sahin.com");
            memberEntity1.setLibraryCard(libraryCardEntity4);

            MemberEntity memberEntity2 = new MemberEntity();
            memberEntity2.setName("Matt");
            memberEntity2.setSurname("Thogh");
            memberEntity2.setEmail("mattt@sahin.com");
            memberEntity2.setLibraryCard(libraryCardEntity5);

            librarianRepository.save(librarianEntity1);
            librarianRepository.save(librarianEntity2);
            librarianRepository.save(librarianEntity3);
            memberRepository.save(memberEntity1);
            memberRepository.save(memberEntity2);
        }
    }
}
