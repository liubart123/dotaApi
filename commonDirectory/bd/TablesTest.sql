
select * from ITEMS where id = 1;
select * from ITEMS;
select * from HEROES;
select * from ROLES;
select * from HeroesRoles;
select * from MATCHES;
select * from PlayersMatches;
select * from PlayerMatchTimeStat;
select count(*) from PlayerMatchTimeStat;
select * from BoughtItems;
select count(*) from BoughtItems;



--dwh
CREATE TABLE TEST_GPM_TABLE(
  match_id INT,
	win NUMBER(1) CHECK (win IN (1,0)) ,
  GPM_SUM INT,
  XP_SUM INT);
alter table TEST_GPM_TABLE add constraint test_gpm_pk0 primary key (match_id,win);
drop table TEST_GPM_TABLE purge;
truncate table TEST_GPM_TABLE ;

SELECT 
  PLAYERSMATCHES.MATCH_ID ,
  PLAYERSMATCHES.WIN,
  (SUM(PLAYERMATCHTIMESTAT.GOLD))  
FROM 
  PDB_KURS_DWH_ADMIN.PLAYERSMATCHES PLAYERSMATCHES  INNER JOIN  PDB_KURS_ADMIN2.PLAYERMATCHTIMESTAT@"testSource" PLAYERMATCHTIMESTAT  
    ON  PLAYERMATCHTIMESTAT.PLAYER_MATCH_ID=PLAYERSMATCHES.PLAYER_MATCH_ID
WHERE
  (PLAYERMATCHTIMESTAT.TIME=10
) 
GROUP BY
  PLAYERSMATCHES.WIN_RADIANT, PLAYERSMATCHES.MATCH_ID
order by match_id;




SELECT 
  PLAYERSMATCHES.MATCH_ID ,
  PLAYERSMATCHES.WIN_RADIANT 
FROM 
  PDB_KURS_DWH_ADMIN.PLAYERSMATCHES PLAYERSMATCHES  INNER JOIN  PDB_KURS_ADMIN2.PLAYERMATCHTIMESTAT@"testSource" PLAYERMATCHTIMESTAT  
    ON  PLAYERMATCHTIMESTAT.PLAYER_MATCH_ID=PLAYERSMATCHES.PLAYER_MATCH_ID
WHERE
  (PLAYERMATCHTIMESTAT.TIME=10
) 
order by match_id;

truncate table pdb_kurs_admin2.heroesroles;



select * from playersmatches where player_match_id=153;
select * from pdb_kurs_admin2.PLAYERMATCHTIMESTAT where player_match_id=153;