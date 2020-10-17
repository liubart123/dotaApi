CREATE TABLE Matches (
	id NUMBER(12, 0) NOT NULL,
	duration INT NOT NULL,
	dire_score INT NOT NULL,
	radiant_score INT NOT NULL,
	skill INT,
	version INT,
	win NUMBER(1) CHECK (win IN (1,0)) NOT NULL,
	constraint MATCHES_PK PRIMARY KEY (id));
/
--drop table Matches purge;

CREATE TABLE Players (
	id NUMBER(11, 0) NOT NULL,
	constraint PLAYERS_PK PRIMARY KEY (id));
--alter table Players modify id NUMBER(11, 0);

/
--alter table PlayersMatches modify player_match_id NUMBER(23, 0) ;
--alter table PlayersMatches modify player_id NUMBER(11, 0) ;
CREATE TABLE PlayersMatches (
	player_id NUMBER(11, 0) NOT NULL,
	match_id NUMBER(12, 0) NOT NULL,
	hero_id INT NOT NULL,
	kills INT NOT NULL,
	deaths INT NOT NULL,
	assists INT NOT NULL,
	damage INT NOT NULL,
	healing INT NOT NULL,
	tower_damage INT NOT NULL,
	teamfight FLOAT NOT NULL,
	player_match_id NUMBER(23, 0) NOT NULL,
	tower_kills INT NOT NULL,
	courier_kills INT NOT NULL,
	sentry_kills INT NOT NULL,
	sentry_builds INT NOT NULL,
	observer_kills INT NOT NULL,
	observer_builds INT NOT NULL,
	camps_stacked INT NOT NULL,
	creep_kills INT NOT NULL,
	creep_denies INT NOT NULL,
	stunning FLOAT NOT NULL,
	lane_efficiency INT NOT NULL,
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
	id INT NOT NULL,
	name VARCHAR2(30) NOT NULL,
	constraint HEROES_PK PRIMARY KEY (id));


/
CREATE TABLE Items (
	id INT NOT NULL,
	name VARCHAR2(40) NOT NULL,
	description VARCHAR2(1000) NOT NULL,
  key_name varchar(50),
	constraint ITEMS_PK PRIMARY KEY (id));

--alter table Items add key_name varchar(50);
--alter table Items modify description VARCHAR2(1000);

/
--alter table BoughtItems modify player_match_id NUMBER(23, 0);
CREATE TABLE BoughtItems (
	item_id INT NOT NULL,
	player_match_id NUMBER(23, 0) NOT NULL,
	timing INT NOT NULL);


/
--alter table PlayerMatchTimeStat modify player_match_id NUMBER(23, 0);
CREATE TABLE PlayerMatchTimeStat (
	player_match_id NUMBER(23, 0) NOT NULL,
	time INT NOT NULL,
	gold INT NOT NULL,
	xp INT NOT NULL,
	constraint PLAYER_MATCH_STAT_PK PRIMARY KEY (player_match_id,time));


/
CREATE TABLE Roles (
	name VARCHAR2(30) UNIQUE NOT NULL,
	id INT NOT NULL,
	constraint ROLES_PK PRIMARY KEY (id));


/
CREATE TABLE HeroesRoles (
	hero_id INT NOT NULL,
	role_id INT NOT NULL);

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