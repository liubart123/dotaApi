CREATE TABLE Matches (
	id NUMBER(11) NOT NULL,
	duration INT NOT NULL,
	dire_score INT NOT NULL,
	radiant_score INT NOT NULL,
	skill INT,
	version INT,
  winRadiant NUMBER(1) 
  CHECK (winRadiant IN (0,1)),
	constraint MATCHES_PK PRIMARY KEY (id)) tablespace main_ts;
/

CREATE TABLE Players (
	id INT NOT NULL,
	constraint PLAYERS_PK PRIMARY KEY (id)) tablespace main_ts;

/

CREATE TABLE PlayersMatches (
	player_id NUMBER(9) NOT NULL,
	match_id NUMBER(11) NOT NULL,
	hero_id INT NOT NULL,
	kills INT NOT NULL,
	deaths INT NOT NULL,
	assists INT NOT NULL,
	damage INT NOT NULL,
	healing INT NOT NULL,
	tower_damage INT NOT NULL,
	teamfight FLOAT NOT NULL,
	player_match_id NUMBER(20) NOT NULL,
	tower_kills INT NOT NULL,
	courier_kills INT NOT NULL,
	sentry_kills INT NOT NULL,
	sentry_builds INT NOT NULL,
	observer_kills INT NOT NULL,
	observer_builds INT NOT NULL,
	camps_stucked INT NOT NULL,
	creep_kills INT NOT NULL,
	creep_denies INT NOT NULL,
	stunning FLOAT NOT NULL,
  constraint PLAYERS_MATCHES_PK PRIMARY KEY (player_match_id))  tablespace main_ts;
/
alter table PlayersMatches add 
  lane_efficiency INT NOT NULL;
CREATE sequence PLAYERSMATCHES_PK_SEQ;
/
CREATE trigger PLAYERSMATCHES_PK_SEQ
  before insert on PlayersMatches
  for each row
begin
  select PLAYERSMATCHES_PK_SEQ.nextval into :NEW.player_match_id from dual;
end;

/
CREATE TABLE Heroes (
	id INT NOT NULL,
	name VARCHAR2(255) NOT NULL,
	description VARCHAR2(255) NOT NULL,
	constraint HEROES_PK PRIMARY KEY (id))  tablespace main_ts;
/

CREATE TABLE Items (
	id INT NOT NULL,
	name VARCHAR2(255) NOT NULL,
	description VARCHAR2(255) NOT NULL,
	constraint ITEMS_PK PRIMARY KEY (id))  tablespace main_ts;
/


CREATE TABLE BoughtItems (
	item_id INT NOT NULL,
	player_match_id INT NOT NULL,
	timing INT NOT NULL)  tablespace main_ts;

/

CREATE TABLE PlayerMatchTimeStat (
	player_match_id INT NOT NULL,
	time INT NOT NULL,
	gold INT NOT NULL,
	xp INT NOT NULL) tablespace main_ts;

/

CREATE TABLE SkillBuilds (
	player_match_id INT NOT NULL,
	lvl INT NOT NULL,
	skill_number INT NOT NULL)  tablespace main_ts;
/
CREATE TABLE Skills (
	id INT NOT NULL,
	description VARCHAR2(255) NOT NULL,
	hero_id INT NOT NULL,
	is_ultimate CHAR(1) CHECK (is_ultimate IN ('N','Y')) NOT NULL,
	constraint SKILLS_PK PRIMARY KEY (id))  tablespace main_ts;
/


--CREATE TABLE Roles (
	--name VARCHAR2(255) NOT NULL)  tablespace main_ts;
--/


ALTER TABLE PlayersMatches ADD CONSTRAINT PlayersMatches_fk0 FOREIGN KEY (player_id) REFERENCES Players(id);
ALTER TABLE PlayersMatches ADD CONSTRAINT PlayersMatches_fk1 FOREIGN KEY (match_id) REFERENCES Matches(id);
ALTER TABLE PlayersMatches ADD CONSTRAINT PlayersMatches_fk2 FOREIGN KEY (hero_id) REFERENCES Heroes(id);



ALTER TABLE BoughtItems ADD CONSTRAINT BoughtItems_fk0 FOREIGN KEY (item_id) REFERENCES Items(id);
ALTER TABLE BoughtItems ADD CONSTRAINT BoughtItems_fk1 FOREIGN KEY (player_match_id) REFERENCES PlayersMatches(player_match_id);

ALTER TABLE PlayerMatchTimeStat ADD CONSTRAINT PlayerMatchTimeStat_fk0 FOREIGN KEY (player_match_id) REFERENCES PlayersMatches(player_match_id);

ALTER TABLE SkillBuilds ADD CONSTRAINT SkillBuilds_fk0 FOREIGN KEY (player_match_id) REFERENCES PlayersMatches(player_match_id);

ALTER TABLE Skills ADD CONSTRAINT Skills_fk0 FOREIGN KEY (hero_id) REFERENCES Heroes(id);

--select * from dba_tables where tablespace_name = 'MAIN_TS';


--SELECT * FROM USER_CONSTRAINTS WHERE owner = 'PDB_KURS_ADMIN2';
--alter session set container=  pdb_kurs;
--select * from Mathces;
drop table Skills CASCADE CONSTRAINTS purge;
drop table PlayerMatchTimeStat CASCADE CONSTRAINTS purge;
drop table BoughtItems CASCADE CONSTRAINTS purge;
drop table ITEMS CASCADE CONSTRAINTS purge;
drop table Heroes CASCADE CONSTRAINTS purge;
drop table PlayersMatches CASCADE CONSTRAINTS purge;
drop table PLAYERS CASCADE CONSTRAINTS purge;
drop table SkillBuilds CASCADE CONSTRAINTS purge;
drop table Players purge;
drop table Matches purge;
drop sequence PLAYERSMATCHES_PK_SEQ;
drop table Roles purge;



