---------------------------------ITEMS

create or replace procedure INSERT_ITEM 
  (item_id in INT, item_name in varchar2, item_description in varchar2, item_key_name in varchar2) IS
  exist PLS_INTEGER;
  begin
    SELECT COUNT(1)
      INTO exist
      FROM ITEMS
     WHERE id = item_id
       AND ROWNUM = 1;
    if exist = 1 then
      update ITEMS set  name = item_name,description = item_description, key_name = item_key_name where id=item_id;
    else 
        insert into ITEMS (id, name, description, key_name)
      values 
        (item_id, item_name, item_description, item_key_name);
    end if;
  end;
/
--drop procedure INSERT_ITEM;
/
begin
  INSERT_ITEM(1, 'her', 'desc2');
end;
/
create or replace procedure SELECT_ITEMS(curs out SYS_REFCURSOR) is
  begin
    open curs for select * from ITEMS;
  end;
/
select * from ITEMS order by id desc;
--------------------------------------------HERO ROLE

create or replace procedure INSERT_HERO_ROLE 
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
create or replace procedure SELECT_HERO_ROLES(curs out SYS_REFCURSOR) is
  begin
    open curs for select * from Roles;
  end;
/
select * from Roles;
--drop procedure INSERT_HERO_ROLE;
/

--uniting heroes and role_hero
create or replace procedure INSERT_HEROES_ROLES
  (phero_id in INT, prole_id in INT) IS
  exist PLS_INTEGER;
  begin
    SELECT COUNT(1)
      INTO exist
      FROM HeroesRoles
     WHERE hero_id = phero_id and role_id = prole_id
       AND ROWNUM = 1;
    if exist <> 1 then
        insert into HeroesRoles (hero_id, role_id)
      values 
        (phero_id, prole_id);
    end if;
  end;
/
create or replace procedure SELECT_HEROES_ROLES(curs out SYS_REFCURSOR) is
  begin
    open curs for select * from HeroesRoles;
  end;
/
select * from HeroesRoles ;
--drop procedure INSERT_HEROES_ROLES;
/
create or replace procedure CLEAR_HEROES_ROLES
  is
  begin
    execute immediate 'truncate table HeroesRoles';
  end;
/
--drop procedure CLEAR_HEROES_ROLES;
/
begin 
  --CLEAR_HEROES_ROLES;
end;
/
--------------------------------------------HERO ROLE

create or replace procedure INSERT_HERO 
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
create or replace procedure SELECT_HEROES(curs out SYS_REFCURSOR) is
  begin
    open curs for select * from Heroes;
  end;
/
select * from Heroes;
--drop procedure INSERT_HERO;
/
begin
  --INSERT_HERO(1, 'hero1');
end;
/
begin
  --INSERT_HERO_ROLE(1, 'herorole1');
end;


/

