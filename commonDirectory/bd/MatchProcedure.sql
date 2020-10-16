--------------------------------------------insert match
create procedure INSERT_MATCH
  ( match_id NUMBER,
    duration INT,
    dire_score INT,
    radiant_score INT,
    skill INT,
    version INT,
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
          skill,
          version,
          win );
    end if;
  end;
/
drop procedure INSERT_MATCH;