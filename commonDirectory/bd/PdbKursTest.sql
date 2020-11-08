select * from user_objects;
select * from user_tables;
select * from dba_users;

select * from heroes;
insert into heroes values (2228, 'insertTest');
delete from heroes where id = 2228;
delete from pdb_kurs_dwh_admin.heroes where id = 2228;
select * from pdb_kurs_dwh_admin.heroes order by id desc;
select * from heroes order by id desc;
truncate table  pdb_kurs_dwh_admin.heroes ;

select count(*) from matches;
select * from matches where id = 5679244991;
select count(*) from PlayersMatches;
select * from 