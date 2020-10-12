

create procedure INSERT_ITEM 
  (item_id in INT, item_name in varchar2, item_description in varchar2) IS
  exist PLS_INTEGER;
  begin
    SELECT COUNT(1)
      INTO exist
      FROM ITEMS
     WHERE id = item_id
       AND ROWNUM = 1;
    if exist = 1 then
      update ITEMS set  name = item_name,description = item_description where id=item_id;
    else 
        insert into ITEMS (id, name, description)
      values 
        (item_id, item_name, item_description);
    end if;
  end;
/
drop procedure INSERT_ITEM;
/
begin
  INSERT_ITEM(1, 'her', 'desc2');
end;
/
create procedure INSERT_HERO_ROLE 
  (role_id in INT, role_name in varchar2) IS
  exist PLS_INTEGER;
  begin
    SELECT COUNT(1)
      INTO exist
      FROM ROLES
     WHERE id = role_id
       AND ROWNUM = 1;
    if exist = 1 then
      update ROLES set name = role_name where id=role_id;
    else 
        insert into ROLES (name, id)
      values 
        (role_name, role_id);
    end if;
  end;
/
drop procedure INSERT_HERO_ROLE;
/

create procedure INSERT_HERO 
  (hero_id in INT, hero_name in varchar2) IS
  exist PLS_INTEGER;
  begin
    SELECT COUNT(1)
      INTO exist
      FROM HEROES
     WHERE id = hero_id
       AND ROWNUM = 1;
    if exist = 1 then
      update HEROES set name = hero_name where id=hero_id;
    else 
        insert into HEROES (name, id)
      values 
        (hero_name, hero_id);
    end if;
  end;
/
drop procedure INSERT_HERO;
/
begin
  INSERT_HERO(1, 'hero1');
end;
/
begin
  INSERT_HERO_ROLE(1, 'herorole1');
end;
