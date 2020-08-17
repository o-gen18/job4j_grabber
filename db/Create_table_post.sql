drop table if exists post;
create table post(
id serial primary key,
name varchar(2000),
text text,
link text UNIQUE,
created timestamp,
author varchar(2000),
author_URL text,
last_commented timestamp
);
