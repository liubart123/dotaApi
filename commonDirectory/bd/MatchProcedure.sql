--------------------------------------------insert match
create or replace procedure INSERT_MATCH
  ( match_id NUMBER,
    duration INT,
    dire_score INT,
    radiant_score INT,
    skill_p INT,
    version_p INT,
    win INT)
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
          win 
          )
      values 
        (match_id,
          duration,
          dire_score,
          radiant_score,
          skill_p,
          version_p,
          win );
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