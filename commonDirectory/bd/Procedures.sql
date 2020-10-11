insert into HEROES values 
  (12345, 'hero1', 'desc1');

  /
create procedure INSERT_HERO 
  (hero_id in INT, hero_name in varchar2, hero_description in varchar2) IS
  begin
    insert into HEROES (id, name, description)
    values 
      (hero_id, hero_name, hero_description);
  end;
/
drop procedure INSERT_HERO;
/
begin
  INSERT_HERO(12346, 'hero2', 'desc2');
end;