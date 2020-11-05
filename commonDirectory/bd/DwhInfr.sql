
--DWH
alter session set container=pdb_kurs;
create user pdb_kurs_dwh_admin identified  by password;
grant resource, connect, DBA to pdb_kurs_dwh_admin;
alter user pdb_kurs_dwh_admin default role all;
create tablespace dwh_ts datafile 'C:\kp\tablespaces\dwh_ts.dbf' SIZE 50M AUTOEXTEND on maxsize unlimited  online extent management local;

alter user pdb_kurs_dwh_admin default tablespace dwh_ts;



