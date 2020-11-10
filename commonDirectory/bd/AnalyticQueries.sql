select heroes.name,heroes.id, count(*) fame from playersmatches join heroes on hero_id = heroes.id group by heroes.id, heroes.name order by fame desc;
/
select count(*) from playersmatches;
select count(*) from pdb_kurs_admin2.playersmatches;


select durationmin, avg(win), count(*) from playersmatches where hero_id in (128,19,129,86) group by durationmin order by durationmin desc;
select round(durationmin/2)*2 as dur from playersmatches where hero_id in (128,19,129,86) group by dur order by durationmin desc;
select * from heroes;
select * from playersmatches join heroes on hero_id = heroes.id order by hero_id desc;

select * from dba_tablespaces;
select tablespace_name, segment_type, sum(Bytes)/1024/1024 as migabytes from dba_segments where tablespace_name in ('MAIN_TS','DWH_TS') group by tablespace_name, segment_type;



select count(*) from playersmatches;
select heroes.name xaxis, 
  sum(kills+assists)/(sum(deaths)+1)
  kda, count(*) from pdb_kurs_dwh_admin.playersmatches join heroes on hero_id = heroes.id group by heroes.name order by kda desc;



select enemyHeroes.name, avg(my.win) as winrate, count(*) as matches from 
  playersmatches my 
  join playersmatches enemy 
  on my.match_id=enemy.match_id 
    and my.win <> enemy.win 
  join playersmatches enemy2
  on enemy.match_id = enemy2.match_id
    and enemy2.win = enemy.win
    and enemy2.hero_id <> enemy.hero_id
  join heroes myHeroes
    on my.hero_id = myHeroes.id
  join heroes enemyHeroes
    on enemy.hero_id = enemyHeroes.id
  join heroes enemy2Heroes
    on enemy2.hero_id = enemy2Heroes.id
  where myHeroes.id = 128
    and enemy2Heroes.id = 86
  group by enemyHeroes.name
  order by winrate desc;
    
select * from heroes where name = 'Rubick';
/
select * from heroes;
select distinct version from playersmatches;
select DURATIONMIN as xaxis from pdb_kurs_dwh_admin.playersmatches group by xaxis;

SELECT floor(my.DURATIONMIN/3)*3 AS xAxis,
    AVG(my.win)         AS yAxis,
    COUNT(*)            AS selection
  FROM pdb_kurs_dwh_admin.playersmatches my
  WHERE my.hero_id      = 4
GROUP BY floor(my.DURATIONMIN/3)*3 order by xaxis;


SELECT floor(my.DURATIONMIN/4)*4 AS xAxis,
  AVG(my.win)                        AS yAxis,
  COUNT(*)                           AS selection
FROM pdb_kurs_dwh_admin.playersmatches my
WHERE my.hero_id=1
GROUP BY floor(my.DURATIONMIN/4)*4
ORDER BY xAxis;

select distinct version from pdb_kurs_dwh_admin.playersmatches;
select min(start_date) from pdb_kurs_dwh_admin.playersmatches;
select count (*) from playersmatches;
select count (*) from pdb_kurs_dwh_admin.playersmatches;

SELECT hers.name,
  COUNT(*)                           AS selection
FROM pdb_kurs_dwh_admin.playersmatches my join pdb_kurs_dwh_admin.heroes hers on 
  my.hero_id = hers.id
group by hers.name
order by count(*) desc;

select * from heroes where name like '%St%';

--charts
select * from (
  select hers.name, avg(my.win) winrate, count(*) from
    pdb_kurs_dwh_admin.playersmatches my join
    pdb_kurs_dwh_admin.playersmatches enemies 
      on my.match_id = enemies.match_id and my.win <> enemies.win
    join pdb_kurs_dwh_admin.playersmatches allies 
      on my.match_id = allies.match_id and my.win = allies.win
    join
    pdb_kurs_dwh_admin.heroes hers 
      on hers.id = allies.hero_id
    where my.hero_id=1 and enemies.hero_id = 60
    group by hers.name, enemies.hero_id
    order by  count(*) desc,winrate desc
    )
  where rownum <=5;
--bars
select alliesHeroes.name,alliesHeroes.id, avg(my.win) winrate, count(*) selection from
  pdb_kurs_dwh_admin.playersmatches my join
  pdb_kurs_dwh_admin.playersmatches enemies 
    on my.match_id = enemies.match_id and my.win <> enemies.win
  join
  pdb_kurs_dwh_admin.heroes hers 
    on hers.id = enemies.hero_id
  join pdb_kurs_dwh_admin.playersmatches allies
    on my.match_id = allies.match_id and my.hero_id <> allies.hero_id and my.win = allies.win
  join pdb_kurs_dwh_admin.heroes alliesHeroes 
    on allies.hero_id = alliesHeroes.id
  where my.hero_id=1 and (
    hers.name = 'Lycan' or 
    hers.name = 'Meepo' or 
    hers.name = 'Night Stalker' or 
    hers.name = 'Vengeful Spirit')
  group by alliesHeroes.name,alliesHeroes.id
  order by selection desc;
    
    
