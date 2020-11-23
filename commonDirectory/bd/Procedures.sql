---------------------------------ITEMS

create or replace procedure INSERT_ITEM 
  (item_id in INT, 
  item_name in varchar2, 
  item_description in varchar2, 
  item_key_name in varchar2, 
  item_img in varchar2) IS
  exist PLS_INTEGER;
  begin
    SELECT COUNT(1)
      INTO exist
      FROM ITEMS
     WHERE id = item_id
       AND ROWNUM = 1;
    if exist = 1 then
      update ITEMS set  
        name = item_name,
        description = item_description, 
        key_name = item_key_name, 
        img = item_img 
      where id=item_id;
    else 
        insert into ITEMS (id, name, description, key_name, img)
      values 
        (item_id, item_name, item_description, item_key_name, item_img);
    end if;
  end;
/
create or replace procedure SELECT_ITEMS(curs out SYS_REFCURSOR) is
  begin
    open curs for select * from ITEMS order by name;
  end;
/
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
    open curs for select * from Roles order by name;
  end;
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
create or replace procedure CLEAR_HEROES_ROLES
  is
  begin
    execute immediate 'truncate table HeroesRoles';
  end;
/
--------------------------------------------HERO 

create or replace procedure INSERT_HERO 
  (hero_id in INT, hero_name in varchar2, img in varchar2, icon in varchar2) IS
  exist PLS_INTEGER;
  begin
    SELECT COUNT(1)
      INTO exist
      FROM HEROES
     WHERE id = hero_id
       AND ROWNUM = 1;
    if exist = 1 then
      update HEROES set 
        name = hero_name,
        imgUrl = img ,
        iconUrl = icon 
        where id=hero_id;
    else 
        insert into HEROES (name, id, imgUrl, iconUrl)
      values 
        (hero_name, hero_id, img, icon);
    end if;
  end;
/
create or replace procedure SELECT_HEROES(curs out SYS_REFCURSOR) is
  begin
    open curs for select * from Heroes order by name;
  end;
/
create or replace procedure ClearAllDb
is begin
  execute immediate 'truncate table boughtItems';
  execute immediate 'truncate table playermatchtimestat';
  execute immediate 'ALTER TABLE BOUGHTITEMS DISABLE CONSTRAINT BOUGHTITEMS_FK1';
  execute immediate 'ALTER TABLE PLAYERMATCHTIMESTAT DISABLE CONSTRAINT PLAYERMATCHTIMESTAT_FK0';
  execute immediate 'truncate table playersmatches';
  execute immediate 'ALTER TABLE BOUGHTITEMS enable CONSTRAINT BOUGHTITEMS_FK1';
  execute immediate 'ALTER TABLE PLAYERMATCHTIMESTAT enable CONSTRAINT PLAYERMATCHTIMESTAT_FK0';
  execute immediate 'ALTER TABLE PLAYERSMATCHES DISABLE CONSTRAINT PLAYERSMATCHES_FK1';
  execute immediate 'truncate table matches';
  execute immediate 'ALTER TABLE PLAYERSMATCHES enable CONSTRAINT PLAYERSMATCHES_FK1';
end;
/

--каб знайсці ўсе спасылкі на пк табліцы
SELECT * FROM USER_CONSTRAINTS WHERE TABLE_NAME = 'PLAYERSMATCHES';
select 'ALTER TABLE '||TABLE_NAME||' DISABLE CONSTRAINT '||CONSTRAINT_NAME||';'
from user_constraints
where R_CONSTRAINT_NAME='MATCHES_PK';
