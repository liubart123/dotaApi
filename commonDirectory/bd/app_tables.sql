alter session set container = pdb_kurs;
create user pdb_kurs_app_admin identified  by password;
grant resource, connect, DBA to pdb_kurs_app_admin;
grant select any table to pdb_kurs_app_admin;
alter user pdb_kurs_app_admin default role all;

create tablespace app_ts datafile 'C:\kp\tablespaces\app_ts.dbf' SIZE 50M AUTOEXTEND on maxsize unlimited  online extent management local;

alter user pdb_kurs_app_admin default tablespace app_ts;
grant select any table to pdb_kurs_app_admin;
grant REFERENCES on pdb_kurs_admin2.heroes to pdb_kurs_app_admin;
grant REFERENCES on pdb_kurs_admin2.items to pdb_kurs_app_admin;


drop table app_users;
create table app_users (
  id integer generated always as identity primary key,
  name varchar(30) unique,
  hash_password varchar(30),
  user_role varchar(30));
  
  
create table selections(
  id integer generated always as identity primary key,
  name varchar(30),
  durationMin integer,
  durationMax integer,
  patchMin integer,
  patchMax integer,
  dateMin date,
  dateMax date,
  investigated_hero_id integer,
  user_id integer,
  constraint selection_hero_fk foreign key (user_id) references app_users(id),
  constraint inv_her_fk foreign key (investigated_hero_id) references pdb_kurs_admin2.heroes(id));
  /
  alter table selections add constraint selection_unique unique(id,user_id);
  /
  create table selections_heroes(
    selection_id integer,
    hero_id integer,
    hero_role varchar(30),
    constraint selections_heroes_unique unique (selection_id, hero_id, hero_role),
    constraint selections_heroes_fk1 foreign key (selection_id) references selections(id),
    constraint selections_heroes_fk2 foreign key (hero_id) references pdb_kurs_admin2.heroes(id)
    );
    
  create table selections_items(
    selection_id integer,
    item_id integer,
    item_role varchar(30),
    constraint selections_items_unique unique (selection_id, item_id, item_role),
    constraint selections_items_fk1 foreign key (selection_id) references selections(id),
    constraint selections_items_fk2 foreign key (item_id) references pdb_kurs_admin2.items(id)
    );
  /
create table BubbleChart(
  id integer generated always as identity primary key,
  minCountOfMatches integer,
  name varchar(30),
  xAxis varchar(30),
  yAxis varchar(30),
  xScale float,
  user_id integer,
  constraint bubblechart_hero_fk foreign key (user_id) references app_users(id));
/
  alter table BubbleChart add constraint BubbleChart_unique unique(id,user_id);
  /
create table selections_bubblechart(
  selection_id integer,
  bubblechart_id integer,
  constraint sel_bubble_fk1 foreign key (selection_id) references selections(id),
  constraint sel_bubble_fk2 foreign key (bubblechart_id) references bubblechart(id),
  constraint sel_bubble_unique unique (selection_id,bubblechart_id));



create table BarChart(
  id integer generated always as identity primary key,
  minCountOfMatches integer,
  name varchar(30),
  xAxis varchar(30),
  yAxis varchar(30),
  user_id integer,
  constraint BarChart_hero_fk foreign key (user_id) references app_users(id));
/

  create table BarChart_labels(
    BarChart_id integer,
    label varchar(30),
    constraint BarChart_labels_unique unique (BarChart_id, label),
    constraint BarChart_labels_fk foreign key (BarChart_id) references BarChart(id)
    );
    /
  alter table BarChart add constraint BarChart_unique unique(id,user_id);
  /
create table selections_barchart(
  selection_id integer,
  barchart_id integer,
  constraint sel_bar_fk1 foreign key (selection_id) references selections(id),
  constraint sel_bar_fk2 foreign key (barchart_id) references BarChart(id),
  constraint sel_bar_unique unique (selection_id,barchart_id));



create table LineChart(
  id integer generated always as identity primary key,
  minCountOfMatches integer,
  name varchar(30),
  xAxis varchar(30),
  yAxis varchar(30),
  countOfLabels integer,
	isDesc NUMBER(1) CHECK (isDesc IN (1,0)),
  selectionId integer,
  user_id integer,
  constraint line_chart_selection_fk foreign key (selectionId) references selections(id),
  constraint LineChart_hero_fk foreign key (user_id) references app_users(id));
  
/
  alter table LineChart add constraint LineChart_unique unique(id,user_id);
  /

  
