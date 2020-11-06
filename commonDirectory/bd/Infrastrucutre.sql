  show user;
  alter session set container=CDB$ROOT;
  alter session set container=pdb_kurs;
  
  select * from dba_users;

--creating pdb
CREATE PLUGGABLE DATABASE pdb_kurs ADMIN USER pdb_kurs_admin IDENTIFIED BY password
FILE_NAME_CONVERT=('C:\APP\LIUBART\ORADATA\ORCL\PDBSEED\','C:\APP\LIUBART\ORADATA\ORCL\PDB_KURS3\');
alter pluggable database pdb_kurs open read write;
alter session set container=pdb_kurs;
  alter session set container=pdb_kurs;
--user
create user pdb_kurs_admin2 identified  by password;
grant resource, connect, DBA to pdb_kurs_admin2;
grant select any table to pdb_kurs_admin2;
alter user pdb_kurs_admin2 default role all;


--removing pdb
alter session set container=cdb$root;
alter pluggable database pdb_kurs close immediate;
alter pluggable database pdb_kurs unplug into 'C:\unpluggedDbs\pdb_kurs.xml';
drop pluggable database pdb_kurs including datafiles;

--select * from dba_sys_privs where GRANTEE = 'PDB_KURS_ADMIN' or GRANTEE = 'PDB_KURS_ADMIN2';

create tablespace main_ts datafile 'C:\kp\tablespaces\main_ts1.dbf' SIZE 50M AUTOEXTEND on maxsize unlimited  online extent management local;

alter user pdb_kurs_admin2 default tablespace main_ts;


--create tablespace main_ts datafile 'C:\kp\tablespaces\main_ts2.dbf' size 50m AUTOEXTEND on maxsize unlimited  online;
drop tablespace main_ts including contents AND DATAFILES;

