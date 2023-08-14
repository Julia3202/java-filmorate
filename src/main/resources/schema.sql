CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID   INT PRIMARY KEY AUTO_INCREMENT,
    USER_EMAIL     VARCHAR(100) NOT NULL,
    USER_LOGIN     VARCHAR(100),
    USER_NAME VARCHAR(100) NOT NULL,
    USER_BIRTHDAY  DATE,
    constraint "USERS_pk"
    primary key (USER_ID)
    );

CREATE TABLE IF NOT EXISTS GENRE
(
    GENRE_ID   INT PRIMARY KEY ,
    GENRE_NAME VARCHAR(50) not null,
    constraint "GENRE_pk"
    primary key (GENRE_ID)
    );

CREATE TABLE IF NOT EXISTS MPA
(
    MPA_ID   INT PRIMARY KEY ,
    MPA_NAME VARCHAR(50) not null,
    constraint "MPA_pk"
    primary key (MPA_ID)
    );

CREATE TABLE IF NOT EXISTS FILMS
(
    FILM_ID          INT PRIMARY KEY AUTO_INCREMENT,
    FILM_NAME        VARCHAR(100) NOT NULL,
    FILM_DESCRIPTION VARCHAR(200) NOT NULL,
    RELEASE_DATE     DATE,
    DURATION         INTEGER,
    LIKES            INTEGER,
    MPA_ID           INTEGER,
    constraint "FILMS_PK"
    primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
    foreign key (MPA_ID) references MPA (MPA_ID)
    );

CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    genre_id INTEGER NOT NULL,
    film_id  INTEGER NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES FILMS (film_id),
    FOREIGN KEY (genre_id) REFERENCES GENRE (genre_id)
    );

create table if not exists FILM_LIKE
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    constraint "FILM_LIKE_FILMS_FILM_ID_fk"
    foreign key (FILM_ID) references FILMS,
    constraint "LIKES_USERS_USER_ID_fk"
    foreign key (USER_ID) references USERS
    );

create table if not exists USER_FRIEND
(
    USER_ID   INTEGER,
    FRIEND_ID INTEGER,
    constraint "USER_FRIEND_USERS_USER_ID_fk"
    foreign key (USER_ID) references USERS,
    constraint "USER_FRIEND_USERS_USER_ID_fk2"
    foreign key (FRIEND_ID) references USERS
    );
