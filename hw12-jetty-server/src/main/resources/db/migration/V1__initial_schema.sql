create table tuser(id bigint auto_increment, age integer,name varchar(255));
create table tphones(id bigint auto_increment, number varchar(255), user_id bigint);
create table taddress(id bigint auto_increment, street varchar(255), user_id bigint);


