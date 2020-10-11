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