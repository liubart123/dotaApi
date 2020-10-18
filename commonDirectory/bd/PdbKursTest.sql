select * from all_tables where tablespace_name = 'MAIN_TS';
select * from user_tables where tablespace_name = 'MAIN_TS';
select * from all_tables;
select * from all_tables where owner = 'PDB_KURS_ADMIN2';
select * from all_tables where owner = 'PDB_KURS_ADMIN';
select * from dba_tables where tablespace_name = 'main_ts'; 
select * from dba_tables where owner = 'PDB_KURS_ADMIN2'; 

select * from dba_objects where object_name like '%MATCHES_PK%';
select * from dba_objects where owner = 'PDB_KURS_ADMIN2';

alter session set container=cdb$root;
alter session set container=pdb_kurs;

select * from v$pdbs;
ALTER PLUGGABLE DATABASE pdb_kurs
   OPEN READ WRITE;
   
select distinct object_type from dba_objects ;
select * from dictionary where table_name like '%proc';

--all objects of user
select * from dba_objects where owner = 'PDB_KURS_ADMIN2';
--procedures
select * from dba_objects where object_type = 'PROCEDURE' and owner = 'PDB_KURS_ADMIN2';

select * from HEROES;


--big tables 
create table ads (t1 NUMBER(9) primary key, t2 NUMBER(9), t3 NUMBER(9), t4 NUMBER(9));
create unique index ads_ind on ads (t2);
create index ads_ind3 on ads (t3);
create table asd (t1 NUMBER(5) primary key, t2 NUMBER(9), foreign key (t2) references ads (t1) on delete cascade);
drop table ads purge;
drop table asd purge;
delete from ads where t1=1;
insert into ads (t1, t2, t3, t4)
      select level, level, level, level
      from dual
     connect by level <= 12345; 
select * from ads;     
truncate table ads;
insert into ads (t1,t2,t3,t4)
  values (1,1,1,1);
insert into ads (t1,t2,t3,t4)
  values (2,2,2,2);
insert into asd (t1,t2)
  values (1,1);
select * from asd;
update ads set t2=3 where t1 = 1;

select * from ads where t1 = 10999998;
select * from ads where t1 = 4999996;
select * from ads where t1 = 11111112;


select * from ads where t2 = 10999998;
select * from ads where t2 = 4999996;
select * from ads where t2 = 1;

select * from ads where t3 = 10999998;
select * from ads where t3 = 4999996;
select * from ads where t3 = 1;

select * from ads where t4 = 10999998;
select * from ads where t4 = 6000;
select 1 from ads where rownum =1 ;
select * from ads where t4 = 1;


create procedure insert_many 
   IS
   var1 number;
  begin
    for looper in 0..10
    loop
      var1 := looper * 1000000;
      insert into ads (t1, t2, t3, t4)
      select level + var1, level +var1, level+var1, level+var1
      from dual
     connect by level <= 999999; 
    end loop;
  end;
/
drop procedure insert_many;

begin 
  insert_many();
end;
/
drop procedure insert_many;


/
begin
  INSERT_ITEM(3, 'her', 'desc2', 'key');
end;
/
  commit;
  /
  rollback;
/
select * from items;
     
