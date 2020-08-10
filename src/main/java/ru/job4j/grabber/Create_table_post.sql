-- create table post(
--id serial primary key,
--name varchar(2000),
--text text,
--link text UNIQUE,
--created timestamp
--);
alter table post add column author varchar(2000);
alter table post add column author_URL text;
alter table post add column last_commented timestamp;