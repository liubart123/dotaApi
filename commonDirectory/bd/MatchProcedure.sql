--------------------------------------------insert match
create or replace procedure INSERT_MATCH
  ( match_id NUMBER,
    duration INT,
    dire_score INT,
    radiant_score INT,
    skill_p INT,
    version_p INT,
    win INT,
    start_date_p DATE)
   IS
  exist PLS_INTEGER;
  begin
    SELECT COUNT(1)
      INTO exist
      FROM MATCHES
     WHERE id = match_id
       AND ROWNUM = 1;
    if exist <> 1 then
        insert into MATCHES (
          id,
          duration,
          dire_score,
          radiant_score,
          skill,
          version,
          win,
          start_date
          )
      values 
        (match_id,
          duration,
          dire_score,
          radiant_score,
          skill_p,
          version_p,
          win,
          start_date_p);
    else 
      update MATCHES set MATCHES.skill = skill_p, MATCHES.version = version_p where MATCHES.id = match_id;
    end if;
  end;
/
begin
  INSERT_MATCH(5660510195, 1760, 8, 34, NULL, NULL, 1);
end;
/
select * from MATCHES;
select * from PlayersMatches;
/
--insert player in match info
create or replace procedure INSERT_PLAYER_IN_MATCH
  ( 
    player_id_p NUMBER,
    match_id_p NUMBER,
    hero_id_p INT,
    kills_p INT,
    deaths_p INT,
    assists_p INT,
    damage_p INT,
    healing_p INT,
    tower_damage_p INT,
    teamfight_p FLOAT,
    player_match_id_p out NUMBER,
    tower_kills_p INT,
    courier_kills_p INT,
    sentry_kills_p INT,
    sentry_builds_p INT,
    observer_kills_p INT,
    observer_builds_p INT,
    camps_stacked_p INT,
    creep_kills_p INT,
    creep_denies_p INT,
    stunning_p FLOAT,
    lane_efficiency_p INT,
    was_new_insert out INT
  )
   IS
  exist PLS_INTEGER;
  exist_player PLS_INTEGER;
  begin
    --check player for existing
    SELECT COUNT(1)
      INTO exist_player
      FROM PLAYERS
     WHERE id = player_id_p
       AND ROWNUM = 1;
    if  exist_player <> 1 then
      insert into PLAYERS (id) values (player_id_p);
    end if;
    --check for existing of a row with same ids
    SELECT COUNT(1)
      INTO exist
      FROM PLAYERSMATCHES
     WHERE match_id = match_id_p and player_id = player_id_p
       AND ROWNUM = 1;
    if exist <> 1 then
        insert into PLAYERSMATCHES (
          player_id,
          match_id,
          hero_id ,
          kills ,
          deaths ,
          assists ,
          damage ,
          healing ,
          tower_damage ,
          teamfight,
          player_match_id,
          tower_kills ,
          courier_kills ,
          sentry_kills ,
          sentry_builds ,
          observer_kills ,
          observer_builds ,
          camps_stacked ,
          creep_kills ,
          creep_denies ,
          stunning,
          lane_efficiency 
        )
      values 
        (
          player_id_p,
          match_id_p,
          hero_id_p ,
          kills_p ,
          deaths_p ,
          assists_p ,
          damage_p ,
          healing_p ,
          tower_damage_p ,
          teamfight_p,
          -1,
          tower_kills_p ,
          courier_kills_p ,
          sentry_kills_p ,
          sentry_builds_p ,
          observer_kills_p ,
          observer_builds_p ,
          camps_stacked_p ,
          creep_kills_p ,
          creep_denies_p ,
          stunning_p,
          lane_efficiency_p 
        );
      player_match_id_p := PLAYERSMATCHES_ID_SEQ.currval;
    else 
      update PLAYERSMATCHES set 
        hero_id  = hero_id_p,
        kills  = kills_p,
        deaths  = deaths_p,
        assists  = assists_p,
        damage  = damage_p,
        healing  = healing_p,
        tower_damage  = tower_damage_p,
        teamfight = teamfight_p,
        tower_kills  = tower_kills_p,
        courier_kills  = courier_kills_p,
        sentry_kills  = sentry_kills_p,
        sentry_builds  = sentry_builds_p,
        observer_kills  = observer_kills_p,
        observer_builds  = observer_builds_p,
        camps_stacked  = camps_stacked_p,
        creep_kills  = creep_kills_p,
        creep_denies  = creep_denies_p,
        stunning = stunning_p,
        lane_efficiency = lane_efficiency_p
      where player_id = player_id_p and match_id = match_id_p;
    SELECT player_match_id
      INTO player_match_id_p
      FROM PLAYERSMATCHES
      WHERE player_id = player_id_p and match_id = match_id_p
       AND ROWNUM = 1;
    end if;
    was_new_insert := exist;
  end;
  --/
  --declare seq number;
  --begin
  --  INSERT_PLAYER_IN_MATCH(123,0,1,0,0,0,0,0,0,0, seq, 0 ,0,0,0,0,0,0,0,0,0,0);
   -- DBMS_OUTPUT.PUT_LINE(seq);
  --end;
  --/
  --select * from Players;
  --truncate table Players;
  --select * from PlayersMatches;
  --truncate table PlayersMatches;
  /
declare 
  asda number(9);
begin
  asda := PLAYERSMATCHES_ID_SEQ.currval;
  DBMS_OUTPUT.PUT_LINE(asda);
  DBMS_OUTPUT.PUT_LINE(PLAYERSMATCHES_ID_SEQ.currval);
end;
/
PLAYERSMATCHES_ID_SEQ.curval;
select PLAYERSMATCHES_ID_SEQ.curval from roles; 

/

--inserting banchmark (gpm,expm)
create or replace procedure INSERT_PLAYER_MATCH_STAT(
  player_match_id_p NUMBER,
	time_p INT,
	gold_p INT,
	xp_p INT) is 
  exist PLS_INTEGER;
  begin
    SELECT COUNT(1)
      INTO exist
      FROM PlayerMatchTimeStat
     WHERE player_match_id = player_match_id_p and time = time_p
       AND ROWNUM = 1;
    if exist <> 1 then
      insert into PlayerMatchTimeStat (player_match_id, time, gold, xp)
        values (player_match_id_p, time_p, gold_p, xp_p);
    else 
      update PlayerMatchTimeStat set gold= gold_p, xp=xp_p where player_match_id = player_match_id_p and time = time_p;
    end if;
  end;
  
  
  /
--inserting bought items
create or replace procedure INSERT_BOUGHT_ITEMS(
  item_id_p NUMBER,
	player_match_id_p NUMBER,
	timing_p INT) is 
  begin
    insert into BoughtItems (item_id, player_match_id, timing)
        values (item_id_p, player_match_id_p, timing_p);
  end;
  /
create or replace procedure CLEAR_BOUGHT_ITEMS(player_match_id_p NUMBER) is
  begin
    delete from BoughtItems where player_match_id = player_match_id_p;
  end;
  /