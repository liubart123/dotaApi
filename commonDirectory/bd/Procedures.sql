insert into ITEMS values 
  (12345, 'item1', 'desc1');
select * from ITEMS;
truncate table ITEMS;

create procedure INSERT_ITEM 
  (item_id in INT, item_name in varchar2, item_description in varchar2) IS
  exist PLS_INTEGER;
  begin
    SELECT COUNT(1)
      INTO exist
      FROM ITEMS
     WHERE id = item_id
       AND ROWNUM = 1;
    if exist = 1 then
      update ITEMS set  name = item_name,description = item_description where id=item_id;
    else 
        insert into ITEMS (id, name, description)
      values 
        (item_id, item_name, item_description);
    end if;
  end;
/
drop procedure INSERT_ITEM;
/
begin
  INSERT_ITEM(12347, 'hero3', 'desc2');
end;