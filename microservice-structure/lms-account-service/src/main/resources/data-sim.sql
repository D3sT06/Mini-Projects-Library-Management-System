insert into library_management.library_card (account_for, active, issued_at, password, barcode) values ('LIBRARIAN', true, 1603612066000, '$2a$10$3iCwKwH2y92i.pokPMg9kenuM7IdptPP8KiBLfxN7d9N.0MYuT7Gy', 'a1111111-1111-1111-1111-111111111111')
insert into library_management.login_type (id, card_barcode, type, type_specific_key) values (1, 'a1111111-1111-1111-1111-111111111111', 'PASSWORD', null)
insert into library_management.login_type (id, card_barcode, type, type_specific_key) values (2, 'a1111111-1111-1111-1111-111111111111', 'FACEBOOK', '100759338537750')
insert into library_management.account (id, created_by, created_at, last_modified_by, last_modified_at, email, library_card_id, name, phone, surname, type) values (1, null, 1603612066000, null, 1603612066000, 'serkans@sahin.com', 'a1111111-1111-1111-1111-111111111111', 'Serkan', null, 'Sahin', 'LIBRARIAN')

insert into library_management.library_card (account_for, active, issued_at, password, barcode) values ('LIBRARIAN', true, 1603612066000, '$2a$10$3iCwKwH2y92i.pokPMg9kenuM7IdptPP8KiBLfxN7d9N.0MYuT7Gy', 'a1111111-1111-1111-1111-111111111112')
insert into library_management.login_type (id, card_barcode, type) values (3, 'a1111111-1111-1111-1111-111111111112', 'PASSWORD')
insert into library_management.account (id, created_by, created_at, last_modified_by, last_modified_at, email, library_card_id, name, phone, surname, type) values (2, null, 1603612066000, null, 1603612066000, 'joewall@mail.com', 'a1111111-1111-1111-1111-111111111112', 'Joe', null, 'Wall', 'LIBRARIAN')

insert into library_management.library_card (account_for, active, issued_at, password, barcode) values ('LIBRARIAN', true, 1603612066000, '$2a$10$3iCwKwH2y92i.pokPMg9kenuM7IdptPP8KiBLfxN7d9N.0MYuT7Gy', 'a1111111-1111-1111-1111-111111111113')
insert into library_management.login_type (id, card_barcode, type) values (4, 'a1111111-1111-1111-1111-111111111113', 'PASSWORD')
insert into library_management.account (id, created_by, created_at, last_modified_by, last_modified_at, email, library_card_id, name, phone, surname, type) values (3, null, 1603612066000, null, 1603612066000, 'johndoe@mail.com', 'a1111111-1111-1111-1111-111111111113', 'John', null, 'Doe', 'LIBRARIAN')

insert into library_management.library_card (account_for, active, issued_at, password, barcode) values ('MEMBER', true, 1603612066000, '$2a$10$3iCwKwH2y92i.pokPMg9kenuM7IdptPP8KiBLfxN7d9N.0MYuT7Gy', 'a1111111-1111-1111-1111-111111111114')
insert into library_management.login_type (id, card_barcode, type) values (5, 'a1111111-1111-1111-1111-111111111114', 'PASSWORD')
insert into library_management.account (id, created_by, created_at, last_modified_by, last_modified_at, email, library_card_id, name, phone, surname, type) values (4, null, 1603612066000, null, 1603612066000, 'alexwatt@mail.com', 'a1111111-1111-1111-1111-111111111114', 'Alex', null, 'Watt', 'MEMBER')

insert into library_management.library_card (account_for, active, issued_at, password, barcode) values ('MEMBER', true, 1603612066000, '$2a$10$3iCwKwH2y92i.pokPMg9kenuM7IdptPP8KiBLfxN7d9N.0MYuT7Gy', 'a1111111-1111-1111-1111-111111111115')
insert into library_management.login_type (id, card_barcode, type) values (6, 'a1111111-1111-1111-1111-111111111115', 'PASSWORD')
insert into library_management.account (id, created_by, created_at, last_modified_by, last_modified_at, email, library_card_id, name, phone, surname, type) values (5, null, 1603612066000, null, 1603612066000, 'joewall@mail.com', 'a1111111-1111-1111-1111-111111111115', 'Wall', null, 'Orgh', 'MEMBER')

insert into library_management.library_card (account_for, active, issued_at, password, barcode) values ('MEMBER', true, 1603612066000, '$2a$10$3iCwKwH2y92i.pokPMg9kenuM7IdptPP8KiBLfxN7d9N.0MYuT7Gy', 'a1111111-1111-1111-1111-111111111116')
insert into library_management.login_type (id, card_barcode, type) values (7, 'a1111111-1111-1111-1111-111111111116', 'PASSWORD')
insert into library_management.account (id, created_by, created_at, last_modified_by, last_modified_at, email, library_card_id, name, phone, surname, type) values (6, null, 1603612066000, null, 1603612066000, 'joewall@mail.com', 'a1111111-1111-1111-1111-111111111116', 'Matt', null, 'Though', 'MEMBER')
