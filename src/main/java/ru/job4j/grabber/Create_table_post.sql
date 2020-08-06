create table post(
id serial primary key,
name varchar(2000),
text text,
link text UNIQUE,
created timestamp
);