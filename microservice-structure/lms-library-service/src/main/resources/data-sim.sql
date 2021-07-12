insert into library_management.author (id, name, surname) values (1, 'Jose', 'Saramago')
insert into library_management.author (id, name, surname) values (2, 'John', 'Steinbeck')
insert into library_management.author (id, name, surname) values (3, 'George', 'Orwell')
insert into library_management.author (id, name, surname) values (4, 'Stephan', 'Hawking')

insert into library_management.category (id, name) values (1, 'Edebiyat')
insert into library_management.category (id, name) values (2, 'Çocuk ve Gençlik')
insert into library_management.category (id, name) values (3, 'Araştırma/Tarih')
insert into library_management.category (id, name) values (4, 'Felsefe')
insert into library_management.category (id, name) values (5, 'Bilim')

insert into library_management.rack (id, location) values (1, 'A-1')
insert into library_management.rack (id, location) values (2, 'A-2')
insert into library_management.rack (id, location) values (3, 'B-4')

insert into library_management.book (id, author_id, title, publication_date) values (1, 1, 'Körlük', '1990-10-1')
insert into library_management.book (id, author_id, title, publication_date) values (2, 2, 'Fareler ve İnsanlar', '1990-10-1')
insert into library_management.book (id, author_id, title, publication_date) values (3, 3, 'Hayvan Çiftliği', '1995-9-1')
insert into library_management.book (id, author_id, title, publication_date) values (4, 3, '1984', '1985-3-1')
insert into library_management.book (id, author_id, title, publication_date) values (5, 4, 'Zamanın Kısa Tarihi', '2010-7-1')

insert into library_management.book_category (book_id, category_id) values (1, 1)
insert into library_management.book_category (book_id, category_id) values (2, 1)
insert into library_management.book_category (book_id, category_id) values (3, 1)
insert into library_management.book_category (book_id, category_id) values (4, 1)
insert into library_management.book_category (book_id, category_id) values (5, 5)

insert into library_management.book_item (book_id, rack_id, barcode) values (1, 1, 'b1111111-1111-1111-1111-111111111111')
insert into library_management.book_item (book_id, rack_id, barcode) values (1, 1, 'b1111111-1111-1111-1111-111111111112')
insert into library_management.book_item (book_id, rack_id, barcode) values (1, 2, 'b1111111-1111-1111-1111-111111111113')
insert into library_management.book_item (book_id, rack_id, barcode) values (1, 2, 'b1111111-1111-1111-1111-111111111114')
insert into library_management.book_item (book_id, rack_id, barcode) values (2, 1, 'b1111111-1111-1111-1111-111111111115')
insert into library_management.book_item (book_id, rack_id, barcode) values (2, 1, 'b1111111-1111-1111-1111-111111111116')
insert into library_management.book_item (book_id, rack_id, barcode) values (3, 2, 'b1111111-1111-1111-1111-111111111117')
insert into library_management.book_item (book_id, rack_id, barcode) values (4, 2, 'b1111111-1111-1111-1111-111111111118')
insert into library_management.book_item (book_id, rack_id, barcode) values (5, 3, 'b1111111-1111-1111-1111-111111111119')
insert into library_management.book_item (book_id, rack_id, barcode) values (5, 3, 'b1111111-1111-1111-1111-111111111110')


