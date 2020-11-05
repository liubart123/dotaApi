
--DWH
alter session set container=pdb_kurs;
create user pdb_kurs_awm_admin identified  by password default tablespace awm_ts;
grant resource, connect, DBA to pdb_kurs_awm_admin;
alter user pdb_kurs_dwh_admin default role all;
create tablespace awm_ts datafile 'C:\kp\tablespaces\awm_ts_ts.dbf' SIZE 50M AUTOEXTEND on maxsize unlimited  online extent management local;

alter user pdb_kurs_dwh_admin default tablespace awm_ts;

drop table DimProducts purge;
drop table DimDates purge;
drop table DimCustomers purge;
drop table FactSale purge;
drop table DimProdCategories purge;
drop table FactProdCategories purge;
drop procedure sp_DATE_DIMENSION;

--testing
create table DimProducts(name varchar(20), id number primary key);
create table DimDates(date_value date primary key, month varchar(20), day varchar(20));
create table DimCustomers(id number primary key, name varchar(20));
create table FactSale(cost number, custId, prodId, saleDate date, 
    constraint custFk foreign key (custId) references DimCustomers(id),
    constraint dateFk foreign key (saleDate) references DimDates(date_value),
    constraint prodFk foreign key (prodId) references DimProducts(id));
create table DimProdCategories(name varchar(20), id number primary key);
create table FactProdCategories(prodId number, catId number,
      constraint prodCatFk foreign key (prodId) references DimProducts(id),
      constraint catFk foreign key (catId) references DimProdCategories(id));

CREATE OR REPLACE PROCEDURE sp_DATE_DIMENSION (l_start_date DATE, l_end_date DATE) AS
  l_current_date DATE;
BEGIN
  l_current_date := l_start_date;

  WHILE l_current_date <= l_end_date LOOP

    INSERT INTO DimDates VALUES (
      l_current_date, 
      to_char(l_current_date, 'Month'),
      to_char(l_current_date, 'Day') 
    );

    l_current_date := l_current_date + 1;
  END LOOP;
END;
/
declare 
begin
  sp_DATE_DIMENSION(to_date('20190101', 'yyyymmdd'),to_date('20211230', 'yyyymmdd'));
end;
/
select * from DimDates;
select count(*) from DimProducts;



insert into DimProducts
      select 'product' || to_char(level+10), level + 10
      from dual
     connect by level <= 100; 
insert into DimCustomers
      select level + 100, 'customer' || to_char(level+100)
      from dual
     connect by level <= 1000; 
     select to_char(2020+mod(level,2)) || '0' || to_char(mod(level+2,9)+1) || to_char(mod(level+12,19)+10) from dual connect by level < 100;
insert into FactSale
      select mod(level+38,50), mod(level,100)+1, mod(level+5,10)+1, to_date(to_char(2020+mod(level+7,2)) || '0' || to_char(mod(level+8,9)+1) || to_char(mod(level+14,19)+10), 'yyyymmdd')
      from dual
     connect by level <= 1000000; 

insert into DimProdCategories
      select 'ProdCategoriy' || to_char(level), level
      from dual
     connect by level <= 5; 
insert into FactProdCategories
      select mod(level+22,10)+1,mod(level+6,5)+1
      from dual
     connect by level <= 200; 
     
     
--cubes
select * from dba_objects where object_name like '%CUBE%';
select * from all_cubes;
select sum(cost), customers from salescube_view c, PRODUCTS_VIEW p, CUSTOMERS_VIEW cu, DATEDIM_VIEW d where (
p.dim_key = c.products and cu.dim_key = c.customers and c.datedim = d.dim_key and p.level_name = 'PRODUCTS' and d.level_name = 'DATES' and cu.level_name = 'CUSTOMERS') group by c.customers order by sum(cost);

select distinct prodId from FactSale where custId = 15 and cost != 0;
select * from salescube_view where customers ='15' and cost <> 0 and PRODUCTS <> 'ALL_PRODUCTS';
select sum(cost), c.custId from FactSale c, DIMPRODUCTS p, DIMCUSTOMERS cu, DIMDATES d where (
  p.id = c.prodId and cu.id = c.custId and c.saleDate = d.date_value) group by c.custId  order by sum(cost);
  
select c.cost, c.custId from FactSale c, DIMPRODUCTS p, DIMCUSTOMERS cu, DIMDATES d where (
  p.id = c.prodId and cu.id = c.custId and c.saleDate = d.date_value);
select count(*) from FactSale c, DIMPRODUCTS p, DIMCUSTOMERS cu, DIMDATES d where (
  p.id = c.prodId and cu.id = c.custId and c.saleDate = d.date_value);
  
select count (*) from salescube_view where PRODUCTS <> 'ALL_PRODUCTS' and DATEDIM <> 'ALL_DATES' and CUSTOMERS <> 'ALL_CUSTOMERS';
  
select sum(c.cost) from FactSale c, DIMPRODUCTS p, DIMCUSTOMERS cu, DIMDATES d where (
  p.id = c.prodId and cu.id = c.custId and c.saleDate = d.date_value);
  
  
select c.cost from salescube_view c, PRODUCTS_VIEW p, CUSTOMERS_VIEW cu, DATEDIM_VIEW d where (
p.dim_key = c.products and cu.dim_key = c.customers and c.datedim = d.dim_key and p.level_name = 'ALL_PRODUCTS' and d.level_name = 'ALL_DATES' and cu.level_name = 'ALL_CUSTOMERS');

select * from dba_objects where object_name like '%CATEGORIE%';
