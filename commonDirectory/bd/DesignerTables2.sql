CREATE TABLE Matches (
	id NUMBER(12, 0) NOT NULL,
	duration INT NOT NULL,
	dire_score INT NOT NULL,
	radiant_score INT NOT NULL,
	skill INT,
	version INT,
	win BLOB NOT NULL,
	constraint MATCHES_PK PRIMARY KEY (id));


/
CREATE TABLE Players (
	id NUMBER(9, 0) NOT NULL,
	constraint PLAYERS_PK PRIMARY KEY (id));


/
CREATE TABLE PlayersMatches (
	player_id NUMBER(9, 0) NOT NULL,
	match_id NUMBER(12, 0) NOT NULL,
	hero_id INT NOT NULL,
	kills INT NOT NULL,
	deaths INT NOT NULL,
	assists INT NOT NULL,
	damage INT NOT NULL,
	healing INT NOT NULL,
	tower_damage INT NOT NULL,
	teamfight FLOAT NOT NULL,
	player_match_id NUMBER(21, 0) NOT NULL,
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
	description VARCHAR2(255) NOT NULL,
	cost INT NOT NULL,
	constraint ITEMS_PK PRIMARY KEY (id));


/
CREATE TABLE BoughtItems (
	item_id INT NOT NULL,
	player_match_id NUMBER(21, 0) NOT NULL,
	timing INT NOT NULL);


/
CREATE TABLE PlayerMatchTimeStat (
	player_match_id NUMBER(21, 0) NOT NULL,
	time INT NOT NULL,
	gold INT NOT NULL,
	xp INT NOT NULL);


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



ALTER TABLE BoughtItems ADD CONSTRAINT BoughtItems_fk0 FOREIGN KEY (item_id) REFERENCES Items(id);
ALTER TABLE BoughtItems ADD CONSTRAINT BoughtItems_fk1 FOREIGN KEY (player_match_id) REFERENCES PlayersMatches(player_match_id);

ALTER TABLE PLAYERMATCHTIMESTAT ADD CONSTRAINT PLAYERMATCHTIMESTAT_FK0 FOREIGN KEY (PLAYER_MATCH_ID) REFERENCES PLAYERSMATCHES (PLAYER_MATCH_ID);


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