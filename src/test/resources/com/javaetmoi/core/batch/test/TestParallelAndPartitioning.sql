DROP TABLE MASTERPIECE if exists;
DROP TABLE MUSIC_ALBUM if exists;
DROP TABLE MOVIE if exists;

CREATE TABLE MASTERPIECE  (
    MASTERPIECE_ID BIGINT IDENTITY NOT NULL PRIMARY KEY ,    
    NAME VARCHAR(128) NOT NULL ,
    YEAR INT NOT NULL,
    GENRE CHAR(10) NOT NULL ,
);

CREATE TABLE MUSIC_ALBUM  (
    ALBUM_ID BIGINT IDENTITY NOT NULL PRIMARY KEY,
    MASTERPIECE_ID BIGINT NOT NULL,
    BAND VARCHAR(128) NOT NULL,
    constraint ALBUM_MASTERPIECE_ID_FK foreign key (MASTERPIECE_ID)
    references MASTERPIECE(MASTERPIECE_ID)
);

CREATE TABLE MOVIE  (
    MOVIE_ID BIGINT IDENTITY NOT NULL PRIMARY KEY,
    MASTERPIECE_ID BIGINT NOT NULL,
    REALISATOR VARCHAR(128) NOT NULL,
    ACTORS VARCHAR(512) NOT NULL,
    constraint MOVIE_MASTERPIECE_ID_FK foreign key (MASTERPIECE_ID)
    references MASTERPIECE(MASTERPIECE_ID)
) ;

Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (1, 'Help!', 1965, 'Music');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (2, 'Star Wars: Episode IV - A New Hope!', 1977, 'Movie');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (3, 'Outlandos d''Amour!', 1978, 'Music');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (4, 'The Wall', 1979, 'Music');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (5, 'War', 1983, 'Music');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (6, 'Star Wars : Episode VI - Return of the Jedi', 1983, 'Movie');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (7, 'Total Recal', 1990, 'Movie');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (8, 'Achtung Baby', 1991, 'Music');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (9, 'Nevermind', 1991, 'Music');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (10, 'Black Album', 1991, 'Music');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (11, 'Terminator 2 : Judgement Day', 1991, 'Movie');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (12, 'Pulp Fiction', 1994, 'Movie');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (13, 'OK Computer', 1997, 'Music');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (14, 'Showbiz', 1999, 'Music');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (15, 'The Lord of the Rings: The Return of the King', 2003, 'Movie');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (16, 'The Resistance', 2009, 'Music');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (17, 'Inception', 2010, 'Movie');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (18, 'The Dark Knight Rises', 2012, 'Movie');
Insert into MASTERPIECE (MASTERPIECE_ID,NAME,YEAR,GENRE) values (19, 'Messina', 2012, 'Music');

Insert into MUSIC_ALBUM (ALBUM_ID,MASTERPIECE_ID,BAND) values (1, 1, 'The Beatles');
Insert into MUSIC_ALBUM (ALBUM_ID,MASTERPIECE_ID,BAND) values (2, 3, 'The Police');
Insert into MUSIC_ALBUM (ALBUM_ID,MASTERPIECE_ID,BAND) values (3, 4, 'Pink Floyd');
Insert into MUSIC_ALBUM (ALBUM_ID,MASTERPIECE_ID,BAND) values (4, 5, 'U2');
Insert into MUSIC_ALBUM (ALBUM_ID,MASTERPIECE_ID,BAND) values (5, 8, 'U2');
Insert into MUSIC_ALBUM (ALBUM_ID,MASTERPIECE_ID,BAND) values (6, 9, 'Nirvana');
Insert into MUSIC_ALBUM (ALBUM_ID,MASTERPIECE_ID,BAND) values (7, 10, 'Metallica');
Insert into MUSIC_ALBUM (ALBUM_ID,MASTERPIECE_ID,BAND) values (8, 13, 'Radiohead');
Insert into MUSIC_ALBUM (ALBUM_ID,MASTERPIECE_ID,BAND) values (9, 14, 'Muse');
Insert into MUSIC_ALBUM (ALBUM_ID,MASTERPIECE_ID,BAND) values (10, 16, 'Muse');
Insert into MUSIC_ALBUM (ALBUM_ID,MASTERPIECE_ID,BAND) values (11, 19, 'Saez');

Insert into MOVIE (MOVIE_ID,MASTERPIECE_ID,REALISATOR,ACTORS) values (1, 2, 'George Lucas', 'Mark Hamill, Harrison Ford, Carrie Fisher');
Insert into MOVIE (MOVIE_ID,MASTERPIECE_ID,REALISATOR,ACTORS) values (2, 6, 'Richard Marquand', 'Mark Hamill, Harrison Ford, Carrie Fisher');
Insert into MOVIE (MOVIE_ID,MASTERPIECE_ID,REALISATOR,ACTORS) values (3, 7, 'Paul Verhoeven', 'Arnold Schwarzenegger, Sharon Stone');
Insert into MOVIE (MOVIE_ID,MASTERPIECE_ID,REALISATOR,ACTORS) values (4, 11, 'James Cameron', 'Arnold Schwarzenegger');
Insert into MOVIE (MOVIE_ID,MASTERPIECE_ID,REALISATOR,ACTORS) values (5, 12, 'Quentin Tarantino', 'John Travolta, Samuel L. Jackson, Uma Thurman');
Insert into MOVIE (MOVIE_ID,MASTERPIECE_ID,REALISATOR,ACTORS) values (6, 15, 'Peter Jackson', 'Elijah Wood, Sean Astin');
Insert into MOVIE (MOVIE_ID,MASTERPIECE_ID,REALISATOR,ACTORS) values (7, 17, 'Christopher Nolan', 'Leonardo DiCaprio, Marion Cotillard');
Insert into MOVIE (MOVIE_ID,MASTERPIECE_ID,REALISATOR,ACTORS) values (8, 18, 'Christopher Nolan', 'Christian Bale, Gary Oldman');
