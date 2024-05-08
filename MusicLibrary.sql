drop database if exists MusicLibrary;

create database MusicLibrary;
use MusicLibrary;

-- Tables

create table Artist(Artist_ID int primary key, Artist_Name varchar(25));

create table Album(Album_ID int primary key, Album_Name varchar(100), Year int);

create table Song(Song_ID int primary key, Song_name varchar(50), Album_ID int, Genre varchar(50), Duration varchar(10), foreign key song_alb (Album_ID) references Album(Album_ID));

create table AlbArt_ref(Artist_ID int, Album_ID int, Song_ID int, primary key(Artist_ID, Album_ID, Song_ID), foreign key f_art (Artist_ID) references Artist(Artist_ID), foreign key f_alb (Album_ID) references Album(Album_ID), foreign key f_song (Song_ID) references Song(Song_ID) );


-- Artist Values

insert into Artist values(1, "KJ Yesudas");
insert into Artist values(2, "KS Chithra");
insert into Artist values(3, "Vani Jayaram");
insert into Artist values(4, "Gopi Sundar");
insert into Artist values(5, "Mohanlal");
insert into Artist values(6, "Daniel Caeser");
insert into Artist values(7, "Kali Uchis");
insert into Artist values(9, "AR Rahman");
insert into Artist values(10, "Vijay Yesudas");
insert into Artist values(11, "Jithin Raj");

-- Album Values

insert into Album values(101, "Pulimurugan: The Wild Hunter", 2016);
insert into Album values(102, "Freudian", 2017);
insert into Album values(104, "The Goat Life - Aadujeevitham", 2024);

-- Song Values

insert into Song values(1001, "Kaadaniyum", 101, "Indian Film Pop", "3:55");
insert into Song values(1002, "Maanathe Marikurumbe", 101, "Indian Film Pop", "3:10");
insert into Song values(1003, "Muruka Muruka", 101, "Indian Film Pop", "2:39");
insert into Song values(1004, "Malayattoor", 101, "Indian Film Pop", "0:56");

insert into Song values(1005, "Get You", 102, "R&B/Soul", "4:38");
insert into Song values(1006, "Best Part", 102, "R&B/Soul", "3:29");
insert into Song values(1007, "Hold Me Down", 102, "R&B/Soul", "3:51");
insert into Song values(1008, "Neu Roses (Transgressor's Song)", 102, "R&B/Soul", "3:01");
insert into Song values(1009, "Loose", 102, "R&B/Soul", "3:05");
insert into Song values(1010, "We find love", 102, "R&B/Soul", "4:14");
insert into Song values(1011, "Blessed", 102, "R&B/Soul", "4:01");
insert into Song values(1012, "Take Me Away", 102, "R&B/Soul", "3:46");
insert into Song values(1013, "Transform", 102, "R&B/Soul", "4:40");
insert into Song values(1014, "Freudian", 102, "R&B/Soul", "10:02");

insert into Song values(1022, "Omane", 104, "Indian Film Pop", "5:58");
insert into Song values(1024, "Benevolent Breeze", 104, "Indian Film Pop", "5:20");


-- AlbArt_ref values

insert into AlbArt_ref values(1, 101, 1001);
insert into AlbArt_ref values(2, 101, 1001);
insert into AlbArt_ref values(3, 101, 1002);
insert into AlbArt_ref values(4, 101, 1003);
insert into AlbArt_ref values(5, 101, 1004);
insert into AlbArt_ref values(6, 102, 1005);
insert into AlbArt_ref values(6, 102, 1006);
insert into AlbArt_ref values(7, 102, 1006);
insert into AlbArt_ref values(6, 102, 1007);
insert into AlbArt_ref values(6, 102, 1008);
insert into AlbArt_ref values(6, 102, 1009);
insert into AlbArt_ref values(6, 102, 1010);
insert into AlbArt_ref values(6, 102, 1011);
insert into AlbArt_ref values(6, 102, 1012);
insert into AlbArt_ref values(6, 102, 1013);
insert into AlbArt_ref values(6, 102, 1014);
insert into AlbArt_ref values(10, 104, 1022);
insert into AlbArt_ref values(9, 104, 1024);


-- Triggers

-- 1. Referential integrity constraint, deleting tuple value from artist table.

DROP TRIGGER IF EXISTS Artist_deletion;
DELIMITER //

create trigger Artist_deletion
before delete on Artist
for each row
begin
declare artist_count int;
select count(*) into artist_count from AlbArt_ref where Artist_ID=old.Artist_ID;
if artist_count>0 then
signal sqlstate '45000' set message_text = 'Referential integrity constraint violated, cannot delete artist with associated song.';  
end if;
end //

DELIMITER ;

-- using the trigger:

delete from Artist where Artist_ID = 3;

-- EO Trigger - ArtistDeletion


-- View (Playlist)

-- Indian_pop_Playlist

drop view if exists Indian_pop_Playlist;

create view Indian_pop_Playlist as

select Song_name,group_concat(Artist_name ORDER BY Artist_name ASC SEPARATOR ', ') AS Artists,Album_name,Genre,Duration
from song s join albart_ref aa on s.Song_ID=aa.Song_ID
join album a on s.Album_ID =a.Album_ID
join artist ar on ar.Artist_ID=aa.Artist_ID where genre='Indian Film Pop'
group by Song_name, Album_name, Genre, Duration;

-- R&B/Soul_Playlist

drop view if exists RB_Playlist;

create view RB_Playlist as

select Song_name,
GROUP_CONCAT(Artist_name ORDER BY Artist_name ASC SEPARATOR ', ') AS Artists,
Album_name,Genre,Duration
from song s join AlbArt_ref aa ON s.Song_ID = aa.Song_ID
join Album a ON s.Album_ID = a.Album_ID
join Artist ar ON ar.Artist_ID = aa.Artist_ID
where Genre = 'R&B/Soul'group by Song_name, Album_name, Genre, Duration;

-- Created View:

select * from Indian_pop_Playlist;
select * from RB_Playlist;

-- EO Views


-- Procedures:

-- 1. Bulk insertion of an album

drop procedure if exists BulkAlb;

DELIMITER //

create procedure BulkAlb()
begin

declare done int;
declare CONTINUE HANDLER for 1062 set done=1;

insert into Artist values(8, "Abel Tesfaye");
if done=1 then
    select 'Artist_ID already exists';
else
    insert into Album values(103, "My Dear Melancholy,", 2018);

    insert into Song values(1015, "Call out my name", 103, "R&B/Soul", "3:48");
    insert into AlbArt_ref values(8, 103, 1015);

    insert into Song values(1016, "Try Me", 103, "R&B/Soul", "3:41");
    insert into AlbArt_ref values(8, 103, 1016);

    insert into Song values(1017, "Wasted Times", 103, "R&B/Soul", "3:40");
    insert into AlbArt_ref values(8, 103, 1017);

    insert into Song values(1018, "I Was Never There", 103, "R&B/Soul", "4:01");
    insert into AlbArt_ref values(8, 103, 1018);

    insert into Song values(1019, "Hurt You", 103, "R&B/Soul", "3:50");
    insert into AlbArt_ref values(8, 103, 1019);

    insert into Song values(1020, "Privilege", 103, "R&B/Soul", "2:50");
    insert into AlbArt_ref values(8, 103, 1020);

    insert into Song values(1021, "Call out my name - A Capella", 103, "R&B/Soul", "3:44");
    insert into AlbArt_ref values(8, 103, 1021);

end if;
end //

DELIMITER ;
select * from Song;
call BulkAlb();

select * from Song;
-- 2. Search songs from given Album

drop procedure if exists SearchSongsByAlbumName;

DELIMITER //

CREATE PROCEDURE SearchSongsByAlbumName(
    IN AlbumName VARCHAR(100)
)
BEGIN
    DECLARE AlbumCheck VARCHAR(50);

    SELECT Album_Name INTO AlbumCheck
    FROM Album WHERE Album_Name = AlbumName LIMIT 1;

    IF AlbumCheck IS NOT NULL
    THEN
        SELECT s.Song_ID, s.Song_name, s.Genre, s.Duration FROM Song s 
        INNER JOIN Album a ON s.Album_ID = a.Album_ID
        WHERE a.Album_Name = AlbumName;
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Album does not exist';
    END IF;
END//

DELIMITER ;

CALL SearchSongsByAlbumName("Freudian");
CALL SearchSongsByAlbumName("My Dear Melancholy,");

-- 3. Search songs from Artist Name

drop procedure if exists SearchSongsByArtistName;

DELIMITER //

CREATE PROCEDURE SearchSongsByArtistName(
    IN ArtistName VARCHAR(100)
)
BEGIN
    DECLARE ArtistCheck VARCHAR(50);

    SELECT Artist_Name INTO ArtistCheck
    FROM Artist WHERE Artist_Name = ArtistName LIMIT 1;

    IF ArtistCheck IS NOT NULL
    THEN
        SELECT s.Song_ID, s.Song_name, a.Album_Name, s.Genre, s.Duration
        FROM Song s
        INNER JOIN Album a ON s.Album_ID = a.Album_ID
        INNER JOIN AlbArt_ref aa ON s.Song_ID = aa.Song_ID
        INNER JOIN Artist ar ON aa.Artist_ID = ar.Artist_ID
        WHERE ar.Artist_Name = ArtistName;

    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Artist does not exist';
    END IF;

END//

DELIMITER ;

CALL SearchSongsByArtistName("KJ Yesudas");

-- 4. Search songs by Genre

drop procedure if exists SearchSongsByGenre;

DELIMITER //

CREATE PROCEDURE SearchSongsByGenre(
    IN GenreName VARCHAR(50)
)
BEGIN
    DECLARE GenreCheck VARCHAR(50);

    SELECT Genre INTO GenreCheck 
    FROM Song WHERE Genre = GenreName LIMIT 1;

    IF GenreCheck IS NOT NULL
    THEN
        SELECT s.Song_ID, s.Song_name, a.Album_Name, s.Genre, s.Duration
        FROM Song s
        INNER JOIN Album a ON s.Album_ID = a.Album_ID
        WHERE s.Genre = GenreName;
    
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Genre does not exist';
    END IF;
END//

DELIMITER ;

CALL SearchSongsByGenre("R&B/Soul");

-- 5. Insert a new song

DROP procedure IF EXISTS InsertNewSong;

DELIMITER $$

CREATE PROCEDURE InsertNewSong(
    IN SongID INT,
    IN ArtistID INT,
    IN SongName VARCHAR(50),
    IN AlbumName VARCHAR(100),
    IN Genre VARCHAR(50),
    IN Duration VARCHAR(10)
)
BEGIN
    DECLARE AlbumID INT;
    DECLARE ArtistID_Check INT;
 
    SELECT Album_ID INTO AlbumID
    FROM Album
    WHERE Album_Name = AlbumName;

    SELECT Artist_ID INTO ArtistID_Check
    FROM Artist
    WHERE Artist_ID = ArtistID;

    IF AlbumID IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Album does not exist';

    ELSE

        IF ArtistID_Check IS NULL THEN
            SIGNAL SQLSTATE '46000'
            SET MESSAGE_TEXT = 'Artist does not exist';

        ELSE
	    INSERT INTO Song(Song_ID, Song_name, Album_ID, Genre, Duration)
	    VALUES (SongID, SongName, AlbumID, Genre, Duration);
    
	    INSERT INTO AlbArt_ref(Artist_ID, Album_ID, Song_ID)
	    VALUES (ArtistID_Check, AlbumID, SongID);

        END IF;
    END IF;
END $$

DELIMITER ;
CALL InsertNewSong(1023, 11, "Periyone", "The Goat Life - Aadujeevitham", "Indian Film Pop", "5:25");

-- STAT
-- 4 Tables
--    Artist
--    Album
--    Song
--    AlbArt_ref

-- 2 Views
--   Indian_pop_playlist
--   R&B_Playlist
-- 1 Trigger
--    Artist_Deletion
-- 5 Procedures

-- EOF