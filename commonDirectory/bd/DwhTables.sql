create table testTable (butafor Number);
select * from testTable;
select * from testTable2;
create table testTable2 (butafor Number);
insert into testTable2 (butafor) values (122);
truncate table testTable;
select * from user_tables;
drop table BOUGHTITEMS purge;
drop table HEROES purge;
drop table ITEMS purge;
drop table MATCHES purge;
drop table PLAYERS purge;
drop table ROLES purge;
drop table HEROESROLES purge;
drop table PLAYERMATCHTIMESTAT purge;
drop table CUBE_BUILD_LOG purge;
drop table CUBE_OPERATIONS_LOG purge;
drop table CUBE_REJECTED_RECORDS purge;
drop table CUBE_DIMENSION_COMPILE purge;
drop table PLAYERSMATCHES purge;
drop table C$_0PLAYERSMATCHES purge;

--select * from dba_db_links;
create PUBLIC database link "testSource" CONNECT TO pdb_kurs_admin2 IDENTIFIED BY password using '(DESCRIPTION =
    (ADDRESS_LIST =
      (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.0.106)(PORT = 1521))
    )
    (CONNECT_DATA =
      (SERVER = DEDICATED)
      (SERVICE_NAME = pdb_kurs)
    )
  )';
  create database link "testSource" CONNECT TO pdb_kurs_admin2 IDENTIFIED BY password using '(DESCRIPTION =
    (ADDRESS_LIST =
      (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.0.106)(PORT = 1521))
    )
    (CONNECT_DATA =
      (SERVER = DEDICATED)
      (SERVICE_NAME = pdb_kurs)
    )
  )';
drop database link "testSource";
drop public database link "testSource";

drop table PlayersMatches purge;
drop table BoughtItems purge;
drop table PlayersMatches purge;
drop table PlayersMatches purge;
drop table PlayersMatches purge;
/
CREATE TABLE PlayersMatches (
	match_id INT,
	hero_id INT,
	kills INT,
	deaths INT,
	assists INT,
	damage INT,
	healing INT,
	tower_damage INT,
	teamfight FLOAT,
	player_match_id INT,
	tower_kills INT,
	courier_kills INT,
	sentry_kills INT,
	sentry_builds INT,
	observer_kills INT,
	observer_builds INT,
	camps_stacked INT,
	creep_kills INT,
	creep_denies INT,
	stunning FLOAT,
	lane_efficiency INT,
	win NUMBER(1) CHECK (win IN (1,0)) ,
	duration INT,
	durationMin NUMBER,
	dire_score INT,
	radiant_score INT,
	skill INT,
	version INT,
	win_radiant NUMBER(1) CHECK (win_radiant IN (1,0)) ,
	start_date DATE,
	early_gpm_proportion FLOAT,
	early_xpm_proportion FLOAT,
	middle_gpm_proportion FLOAT,
	middle_xpm_proportion FLOAT,
	late_gpm_proportion FLOAT,
	late_xpm_proportion FLOAT,
	last_gpm_proportion FLOAT,
	last_xpm_proportion FLOAT,
  early_gpm_sum NUMBER,
  early_xpm_sum NUMBER,
  middle_gpm_sum NUMBER,
  middle_xpm_sum NUMBER,
  late_gpm_sum NUMBER,
  late_xpm_sum NUMBER,
  last_gpm_sum NUMBER,
  last_xpm_sum NUMBER,
	constraint PLAYERSMATCHES_PK PRIMARY KEY (player_match_id));
/
/
CREATE TABLE Heroes (
	id INT,
	name VARCHAR2(30),
	constraint HEROES_PK PRIMARY KEY (id));


/
CREATE TABLE Items (
	id INT,
	name VARCHAR2(40),
	description VARCHAR2(1000),
	key_name VARCHAR2(50),
	constraint ITEMS_PK PRIMARY KEY (id));


/
CREATE TABLE BoughtItems (
	item_id INT,
	player_match_id INT,
	timing INT);


/
CREATE TABLE Roles (
	name VARCHAR2(30) UNIQUE,
	id INT,
	constraint ROLES_PK PRIMARY KEY (id));


/
CREATE TABLE HeroesRoles (
	hero_id INT,
	role_id INT);

/
CREATE TABLE MatchTimeStat (
	match_id NUMBER ,
	win NUMBER(1) CHECK (win IN (1,0)) ,
	time INT ,
	gold INT ,
	xp INT ,
	constraint MATCH_STAT_PK PRIMARY KEY (match_id,win,time));

/
ALTER TABLE PlayersMatches ADD CONSTRAINT PlayersMatches_fk1 FOREIGN KEY (hero_id) REFERENCES Heroes(id);
alter table PlayersMatches drop constraint PlayersMatches_fk1;


ALTER TABLE BoughtItems ADD CONSTRAINT BoughtItems_fk0 FOREIGN KEY (item_id) REFERENCES Items(id);
ALTER TABLE BoughtItems ADD CONSTRAINT BoughtItems_fk1 FOREIGN KEY (player_match_id) REFERENCES PlayersMatches(player_match_id);


ALTER TABLE HeroesRoles ADD CONSTRAINT HeroesRoles_fk0 FOREIGN KEY (hero_id) REFERENCES Heroes(id);
ALTER TABLE HeroesRoles ADD CONSTRAINT HeroesRoles_fk1 FOREIGN KEY (role_id) REFERENCES Roles(id);

ALTER TABLE HeroesRoles ADD CONSTRAINT HeroesRoles_pk0 primary KEY (role_id, hero_id) ;




/
select * from Heroes;
truncate table HeroesRoles;
truncate table PlayersMatches;
select * from all_objects where object_name like '%SYS_C00242%';