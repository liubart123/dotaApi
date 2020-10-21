CREATE TABLE Matches (
	id NUMBER(12, 0) ,
	duration INT ,
	dire_score INT ,
	radiant_score INT ,
	skill INT,
	version INT,
	win NUMBER(1) CHECK (win IN (1,0)) ,
  start_date DATE ,
	constraint MATCHES_PK PRIMARY KEY (id));
/
--drop table Matches purge;

CREATE TABLE Players (
	id NUMBER(11, 0) ,
	constraint PLAYERS_PK PRIMARY KEY (id));
--alter table Players modify id NUMBER(11, 0);

/
--alter table PlayersMatches modify player_match_id NUMBER(23, 0) ;
--alter table PlayersMatches modify player_id NUMBER(11, 0) ;
CREATE TABLE PlayersMatches (
	player_id NUMBER(11, 0) ,
	match_id NUMBER(12, 0) ,
	hero_id INT ,
	kills INT ,
	deaths INT ,
	assists INT ,
	damage INT ,
	healing INT ,
	tower_damage INT ,
	teamfight FLOAT ,
	player_match_id NUMBER(23, 0) ,
	tower_kills INT ,
	courier_kills INT ,
	sentry_kills INT ,
	sentry_builds INT ,
	observer_kills INT ,
	observer_builds INT ,
	camps_stacked INT ,
	creep_kills INT ,
	creep_denies INT ,
	stunning FLOAT ,
	lane_efficiency INT ,
	win NUMBER(1) CHECK (win IN (1,0)) ,
	constraint PLAYERSMATCHES_PK PRIMARY KEY (player_match_id));

CREATE sequence PLAYERSMATCHES_ID_SEQ;

CREATE trigger BI_PLAYERSMATCHES_ID
  before insert on PlayersMatches
  for each row
begin
  select PLAYERSMATCHES_ID_SEQ.nextval into :NEW.player_match_id from dual;
end;

/
CREATE TABLE Heroes (
	id INT ,
	name VARCHAR2(30) ,
	constraint HEROES_PK PRIMARY KEY (id));


/
CREATE TABLE Items (
	id INT ,
	name VARCHAR2(40) ,
	description VARCHAR2(1000) ,
  key_name varchar(50),
	constraint ITEMS_PK PRIMARY KEY (id));

--alter table Items add key_name varchar(50);
--alter table Items modify description VARCHAR2(1000);

/
--alter table BoughtItems modify player_match_id NUMBER(23, 0);
CREATE TABLE BoughtItems (
	item_id INT ,
	player_match_id NUMBER(23, 0) ,
	timing INT );


/
--alter table PlayerMatchTimeStat modify player_match_id NUMBER(23, 0);
CREATE TABLE PlayerMatchTimeStat (
	player_match_id NUMBER(23, 0) ,
	time INT ,
	gold INT ,
	xp INT ,
	constraint PLAYER_MATCH_STAT_PK PRIMARY KEY (player_match_id,time));


/
CREATE TABLE Roles (
	name VARCHAR2(30) UNIQUE ,
	id INT ,
	constraint ROLES_PK PRIMARY KEY (id));


/
CREATE TABLE HeroesRoles (
	hero_id INT ,
	role_id INT );

/


ALTER TABLE PlayersMatches ADD CONSTRAINT PlayersMatches_fk0 FOREIGN KEY (player_id) REFERENCES Players(id);
ALTER TABLE PlayersMatches ADD CONSTRAINT PlayersMatches_fk1 FOREIGN KEY (match_id) REFERENCES Matches(id);
ALTER TABLE PlayersMatches ADD CONSTRAINT PlayersMatches_fk2 FOREIGN KEY (hero_id) REFERENCES Heroes(id);
--alter table PlayersMatches drop constraint PlayersMatches_fk0;
--alter table PlayersMatches drop constraint PlayersMatches_fk1;
--alter table PlayersMatches drop constraint PlayersMatches_fk2;



--ALTER TABLE BoughtItems ADD CONSTRAINT BoughtItems_fk0 FOREIGN KEY (item_id) REFERENCES Items(id);
ALTER TABLE BoughtItems ADD CONSTRAINT BoughtItems_fk0 FOREIGN KEY (item_id) REFERENCES Items(id) on delete cascade;
--alter table BoughtItems drop constraint BoughtItems_fk0 ;
--ALTER TABLE BoughtItems drop CONSTRAINT BoughtItems_fk0;

ALTER TABLE BoughtItems ADD CONSTRAINT BoughtItems_fk1 FOREIGN KEY (player_match_id) REFERENCES PlayersMatches(player_match_id);
--ALTER TABLE BoughtItems drop CONSTRAINT BoughtItems_fk1;

ALTER TABLE PLAYERMATCHTIMESTAT ADD CONSTRAINT PLAYERMATCHTIMESTAT_FK0 FOREIGN KEY (PLAYER_MATCH_ID) REFERENCES PLAYERSMATCHES (PLAYER_MATCH_ID);
--ALTER TABLE PLAYERMATCHTIMESTAT drop CONSTRAINT PLAYERMATCHTIMESTAT_FK0;


ALTER TABLE HeroesRoles ADD CONSTRAINT HeroesRoles_fk0 FOREIGN KEY (hero_id) REFERENCES Heroes(id);
ALTER TABLE HeroesRoles ADD CONSTRAINT HeroesRoles_fk1 FOREIGN KEY (role_id) REFERENCES Roles(id);



drop table Matches cascade constraints purge;
drop table Players cascade constraints purge;
drop table PlayersMatches  cascade constraints purge;
drop table Heroes cascade constraints purge;
drop table Items cascade constraints purge;
drop table BoughtItems cascade constraints purge;
drop table PlayerMatchTimeStat cascade constraints purge;
drop table Roles cascade constraints purge;
drop table HeroesRoles cascade constraints purge;
drop sequence PLAYERSMATCHES_ID_SEQ;