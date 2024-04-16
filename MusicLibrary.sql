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

-- Album Values

insert into Album values(101, "Pulimurugan: The Wild Hunter", 2016);
insert into Album values(102, "Freudian", 2017);

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

-- Triggers

--1) Referential integrity constraint, deleting tuple value from artist table.


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
end;
//

DELIMITER ;  

-- Procedures

-- Queries

-- 1. Select songs from Freudian Album

select distinct(Song_name, Artist_Name) from Song join AlbArt_ref on Song.Song_ID = AlbArt_ref.Song_ID join Artist on AlbArt_ref.Artist_ID = Artist.Artist_ID where Song.Album_ID = 102;

--+---------------------------------+---------------+
--| Song_name                       | Artist_Name   |
--+---------------------------------+---------------+
--| Get You                         | Daniel Caeser |
--| Best Part                       | Daniel Caeser |
--| Hold Me Down                    | Daniel Caeser |
--| Neu Roses (Transgressor's Song) | Daniel Caeser |
--| Loose                           | Daniel Caeser |
--| We find love                    | Daniel Caeser |
--| Blessed                         | Daniel Caeser |
--| Take Me Away                    | Daniel Caeser |
--| Transform                       | Daniel Caeser |
--| Freudian                        | Daniel Caeser |
--| Best Part                       | Kali Uchis    |
--+---------------------------------+---------------+

-- Ithil Best Part repeat aakathe engane edukkam?
   select distinct(Song_name) from Song join AlbArt_ref on Song.Song_ID =     AlbArt_ref.Song_ID join Artist on AlbArt_r
   ef.Artist_ID = Artist.Artist_ID where Song.Album_ID = 102;

-- EOF