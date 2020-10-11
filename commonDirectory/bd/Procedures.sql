insert into ITEMS values 
  (12345, 'item1', 'desc1');
select * from ITEMS;
truncate table ITEMS;

create procedure INSERT_ITEM 
  (item_id in INT, item_name in varchar2, item_description in varchar2) IS
  begin
    insert into ITEMS (id, name, description)
    values 
      (item_id, item_name, item_description);
  end;
/
drop procedure INSERT_ITEM;
/
begin
  INSERT_ITEM(12346, 'hero2', 'desc2');
end;