delete from FILM_GENRE;
delete from FILM_LIKE;
delete from FILMS;
delete from USER_FRIEND;
delete from USERS;
DELETE FROM GENRE;
DELETE FROM MPA;
INSERT INTO GENRE(GENRE_ID, GENRE_NAME) VALUES ( 1, 'Комедия' );
INSERT INTO GENRE(GENRE_ID, GENRE_NAME) VALUES ( 2, 'Драма' );
INSERT INTO GENRE(GENRE_ID, GENRE_NAME) VALUES ( 3, 'Мультфильм' );
INSERT INTO GENRE(GENRE_ID, GENRE_NAME) VALUES ( 4, 'Триллер' );
INSERT INTO GENRE(GENRE_ID, GENRE_NAME) VALUES ( 5, 'Документальный' );
INSERT INTO GENRE(GENRE_ID, GENRE_NAME) VALUES ( 6, 'Боевик' );

INSERT INTO MPA(MPA_ID, MPA_NAME) VALUES ( 1, 'G' );
INSERT INTO MPA(MPA_ID, MPA_NAME) VALUES ( 2, 'PG' );
INSERT INTO MPA(MPA_ID, MPA_NAME) VALUES ( 3, 'PG-13' );
INSERT INTO MPA(MPA_ID, MPA_NAME) VALUES ( 4, 'R' );
INSERT INTO MPA(MPA_ID, MPA_NAME) VALUES ( 5, 'NC-17' );
