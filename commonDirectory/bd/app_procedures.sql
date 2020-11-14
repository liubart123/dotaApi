--users
create or replace procedure insert_user(nameP varchar, hash_passwordP varchar, user_roleP varchar, resultId out integer)
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM app_users
     WHERE name = nameP
       AND ROWNUM = 1;
    if exist = 1 then
      resultId := -1;
    else 
        insert into app_users (name, hash_password, user_role)
      values 
        (nameP, hash_passwordP, user_roleP);
      select id into resultId from app_users where name = nameP AND ROWNUM = 1;
    end if;
end;
/
create or replace procedure get_user(curs out SYS_REFCURSOR, login varchar) is
  begin
    open curs for select * from app_users where name=login and rownum = 1;
  end;
/
create or replace procedure exist_user(nameP varchar, resultId out boolean)
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM app_users 
     WHERE name = nameP 
       AND ROWNUM = 1;
    if exist = 1 then
      resultId:=true;
    else 
      resultId := false;
    end if;
end;
/
select * from app_users;

--selections
create or replace procedure insert_selection(
  nameP varchar,
  durationMinP int,
  durationMaxP int,
  patchMinP int,
  patchMaxP int,
  dateMinP date,
  dateMaxP date,
  investigated_hero_idP int,
  user_idP int,
  resultId out int)
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM selections 
     WHERE name = nameP and user_id = user_idP
       AND ROWNUM = 1;
    if exist = 1 then
      resultId:=-1;
--      SELECT id
--      INTO resultId
--      FROM selections 
--        WHERE name = nameP and user_id = user_idP
--       AND ROWNUM = 1;
    else 
      insert into selections (
          name,
          durationMin,
          durationMax,
          patchMin,
          patchMax,
          dateMin,
          dateMax,
          investigated_hero_id,
          user_id)
        values (
          nameP,
          durationMinP,
          durationMaxP,
          patchMinP,
          patchMaxP,
          dateMinP,
          dateMaxP,
          investigated_hero_idP,
          user_idP);  
      select id into resultId from selections where name=nameP and user_id=user_idP;
    end if;

end;
/
select * from selections;
select * from selections_heroes;
select * from selections_items;
/
create or replace procedure insert_selection_heroes(
  selection_idp integer,
  hero_idP integer,
  hero_roleP varchar
  )
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM selections_heroes 
     WHERE selection_id = selection_idp and hero_id = hero_idP and hero_role = hero_roleP
       AND ROWNUM = 1;
    if exist <> 1 then
      insert into selections_heroes (
          selection_id,
          hero_id,
          hero_role)
        values (
          selection_idP,
          hero_idP,
          hero_roleP);  
    end if;

end;
/
create or replace procedure clear_selection_heroes(
  selection_idp integer
  )
  is
begin
  delete from selections_heroes where selection_id = selection_idp;
end;
/
create or replace procedure insert_selection_items(
  selection_idp integer,
  item_idP integer,
  item_roleP varchar
  )
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM selections_items 
     WHERE selection_id = selection_idp and item_id = item_idP and item_role = item_roleP
       AND ROWNUM = 1;
    if exist <> 1 then
      insert into selections_items (
          selection_id,
          item_id,
          item_role)
        values (
          selection_idP,
          item_idP,
          item_roleP);  
    end if;

end;
/
create or replace procedure clear_selection_items(
  selection_idp integer
  )
  is
begin
  delete from selections_items where selection_id = selection_idp;
end;
/
create or replace procedure update_selection(
  nameP varchar,
  durationMinP integer,
  durationMaxP integer,
  patchMinP integer,
  patchMaxP integer,
  dateMinP date,
  dateMaxP date,
  investigated_hero_idP integer,
  user_idP integer,
  idP integer,
  resultId out integer)
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM selections 
     WHERE id = idP
       AND ROWNUM = 1;
    if exist = 1 then
      update selections
        set
          name=nameP,
          durationMin=durationMinP,
          durationMax=durationMaxP,
          patchMin=patchMinP,
          patchMax=patchMaxP,
          dateMin=dateMinP,
          dateMax=dateMaxP,
          investigated_hero_id=investigated_hero_idP,
          user_id=user_idP  where id=idp;
      select id into resultId from selections where id=idp;
    else 
      resultId:=-1;
    end if;

end;
/
create or replace procedure delete_selection(
  idP integer)
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM selections 
     WHERE id = idP
       AND ROWNUM = 1;
    if exist = 1 then
      delete from selections where id=idp;
    end if;

end;
/
create or replace procedure SELECT_selections(curs out SYS_REFCURSOR, user_idp integer) is
  begin
    open curs for select * from selections where user_id=user_Idp order by name;
  end;
/
create or replace procedure get_selection(curs out SYS_REFCURSOR, idp integer) is
  begin
    open curs for select * from selections where id=Idp;
  end;
/
create or replace procedure SELECT_selections_items(curs out SYS_REFCURSOR, idp integer) is
  begin
    open curs for select * from selections_items where selection_id=idp;
  end;
/
create or replace procedure SELECT_selections_heroes(curs out SYS_REFCURSOR, idp integer) is
  begin
    open curs for select * from selections_heroes where selection_id=idp;
  end;
/
--BubbleChart

create or replace procedure insert_bubblechart(
  minCountOfMatchesP integer,
  nameP varchar,
  xAxisP varchar,
  yAxisP varchar,
  xScaleP float,
  user_idP integer,
  resultId out integer)
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM BubbleChart 
     WHERE name = nameP and user_id = user_idP
       AND ROWNUM = 1;
    if exist = 1 then
      resultId:=-1;
    else 
      insert into BubbleChart (
          minCountOfMatches,
          name,
          xAxis,
          yAxis,
          xScale,
          user_id)
        values (
          minCountOfMatchesP,
          nameP,
          xAxisP,
          yAxisP,
          xScaleP,
          user_idP);  
      select id into resultId from BubbleChart where name=nameP and user_id=user_idP;
    end if;

end;
/
create or replace procedure insert_selections_bubblechart(
  selection_idP integer,
  bubblechart_idP integer
  )
is
begin
  insert into selections_bubblechart(selection_id,bubblechart_id)
  values (selection_idP,bubblechart_idp);
end;
/
create or replace procedure clear_selections_bubblechart(
  bubblechart_idP integer
  )
  is
begin
  delete from selections_bubblechart where bubblechart_id = bubblechart_idP;
end;
/
create or replace procedure update_bubblechart(
  minCountOfMatchesP integer,
  nameP varchar,
  xAxisP varchar,
  yAxisP varchar,
  xScaleP float,
  user_idP integer,
  idp integer,
  resultId out integer)
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM BubbleChart 
     WHERE id=idp
       AND ROWNUM = 1;
    if exist = 1 then
      update BubbleChart set 
        minCountOfMatches = minCountOfMatchesp,
            name=nameP,
            xAxis=xAxisP,
            yAxis=yAxisP,
            xScale=xScaleP where id=idp;
      resultId:=idp;
    else 
      resultId:=-1;
    end if;

end;
/
create or replace procedure delete_bubblechart(
  idP integer)
is
begin
      delete from bubblechart where id=idp;

end;
/
create or replace procedure SELECT_bubblecharts(curs out SYS_REFCURSOR, user_idp integer) is
  begin
    open curs for select * from bubblechart where user_id=user_Idp order by name;
  end;
/

--BarChart

create or replace procedure insert_barchart(
  minCountOfMatchesP integer,
  nameP varchar,
  xAxisP varchar,
  yAxisP varchar,
  user_idP integer,
  resultId out integer)
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM barChart 
     WHERE name = nameP and user_id = user_idP
       AND ROWNUM = 1;
    if exist = 1 then
      resultId:=-1;
    else 
      insert into barChart (
          minCountOfMatches,
          name,
          xAxis,
          yAxis,
          user_id)
        values (
          minCountOfMatchesP,
          nameP,
          xAxisP,
          yAxisP,
          user_idP);  
      select id into resultId from BubbleChart where name=nameP and user_id=user_idP;
    end if;

end;
/
create or replace procedure insert_selections_barchart(
  selection_idP integer,
  barchart_idP integer
  )
is
begin
  insert into selections_barchart(selection_id,barchart_id)
  values (selection_idP,barchart_idp);
end;
/
create or replace procedure clear_selections_bubblechart(
  barchart_idP integer
  )
  is
begin
  delete from selections_barchart where barchart_id = barchart_idP;
end;
/
create or replace procedure update_barchart(
  minCountOfMatchesP integer,
  nameP varchar,
  xAxisP varchar,
  yAxisP varchar,
  user_idP integer,
  idp integer,
  resultId out integer)
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM barChart 
     WHERE id=idp
       AND ROWNUM = 1;
    if exist = 1 then
      update barChart set 
        minCountOfMatches = minCountOfMatchesp,
            name=nameP,
            xAxis=xAxisP,
            yAxis=yAxisP where id=idp;
      resultId:=idp;
    else 
      resultId:=-1;
    end if;

end;
/
create or replace procedure delete_barchart(
  idP integer)
is
begin
      delete from barchart where id=idp;

end;
/
create or replace procedure SELECT_barcharts(curs out SYS_REFCURSOR, user_idp integer) is
  begin
    open curs for select * from barchart where user_id=user_Idp order by name;
  end;
  /

create or replace procedure insert_barchart_labels(
  BarChart_idp integer,
  labelp varchar
  )
is
begin
  insert into BarChart_labels(BarChart_id,label)
  values (BarChart_idp,labelp);
end;
/
create or replace procedure clear_barchart_labels(
  barchart_idP integer
  )
  is
begin
  delete from BarChart_labels where barchart_id = barchart_idP;
end;

/

--LineChart

create or replace procedure insert_linechart(
  minCountOfMatchesP integer,
  nameP varchar,
  xAxisP varchar,
  yAxisP varchar,
  user_idP integer,
  countOfLabelsp integer,
	isDescp NUMBER,
  selectionIdp integer,
  resultId out integer)
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM linechart 
     WHERE name = nameP and user_id = user_idP
       AND ROWNUM = 1;
    if exist = 1 then
      resultId:=-1;
    else 
      insert into linechart (
          minCountOfMatches,
          name,
          xAxis,
          yAxis,
          countOfLabels,
          isDesc,
          selectionId,
          user_id)
        values (
          minCountOfMatchesP,
          nameP,
          xAxisP,
          yAxisP,
          countOfLabelsp,
          isDescp,
          selectionIdp,
          user_idP);  
      select id into resultId from linechart where name=nameP and user_id=user_idP;
    end if;

end;
/
create or replace procedure update_linechart(
  minCountOfMatchesP integer,
  nameP varchar,
  xAxisP varchar,
  yAxisP varchar,
  countOfLabelsp integer,
	isDescp NUMBER,
  selectionIdp integer,
  user_idP integer,
  idp integer,
  resultId out integer)
is
  exist PLS_INTEGER;
begin
  SELECT COUNT(1)
      INTO exist
      FROM linechart 
     WHERE id=idp
       AND ROWNUM = 1;
    if exist = 1 then
      update linechart set 
        minCountOfMatches = minCountOfMatchesp,
            name=nameP,
            xAxis=xAxisP,
            yAxis=yAxisP,
            countOfLabels=countOfLabelsp,
            isDesc=isDescp,
            selectionId=selectionIdp
            where id=idp;
      resultId:=idp;
    else 
      resultId:=-1;
    end if;

end;
/
create or replace procedure delete_linechart(
  idP integer)
is
begin
      delete from linechart where id=idp;

end;
/
create or replace procedure SELECT_linecharts(curs out SYS_REFCURSOR, user_idp integer) is
  begin
    open curs for select * from linechart where user_id=user_Idp order by name;
  end;
/
