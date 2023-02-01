drop database if exists wolvo_db;

create database wolvo_db;

use wolvo_db;

drop table if exists users;

create table users ( 
	user_id int auto_increment primary key, 
	email varchar(100) unique, 
	first_name varchar(100) not null,
	last_name varchar(100) not null,
	password varchar(100) not null,
	user_type varchar(30) not null,
	privacy varchar(30) not null,
	district varchar(30) not null,
	building_address varchar(100) not null,
	phone_number varchar(20) not null
);
        
drop table if exists friends;

create table friends(
	first_id int NOT NULL, 
	second_id int NOT NULL,
	primary key(first_id, second_id),
    foreign key (first_id) references users(user_id),
	foreign key (second_id) references users(user_id)
);

drop table if exists friend_requests;

create table friend_requests(
	from_id int NOT NULL,
	to_id int NOT NULL, 
    request_status varchar(30) not null,
	PRIMARY KEY (from_id, to_id),
    foreign key (from_id) references users(user_id),
	foreign key (to_id) references users(user_id)
);

drop table if exists couriers;

create table couriers(
	courier_id int auto_increment primary key,
    email varchar(100) unique,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    district varchar(100) not null,
    password varchar(100) not null,
    phone_number varchar(30) not null,
	rating float(3,2) not null,
    raters int not null,
	completed_orders int not null,
	curr_status varchar(30) not null,
    add_status varchar(30) not null,
    curr_order int
);

drop table if exists orders;

create table orders(
	order_id int auto_increment primary key,
	user_id int not null,
	dish_id int not null,
	order_date datetime not null,
    receive_date datetime,
    order_status varchar(30),
	district varchar(30) not null,
	courier_id int not null,
	location varchar(100) not null,
    quantity int not null
);

DROP TABLE IF EXISTS dishes;

CREATE TABLE dishes(
	dish_id int AUTO_INCREMENT primary key,
	name varchar(100) not null,
	rest_id int not null,
	price float(7, 2) not null,
	category varchar(100) not null,
	rating float(3, 2) not null,
    raters int not null,
    add_status varchar(30)
);

DROP TABLE IF EXISTS restaurants;

CREATE TABLE restaurants(
	rest_id int AUTO_INCREMENT primary key,
	name varchar(100) not null,
	manager_id int not null,
	district varchar(30) not null,
	address varchar(100) not null,
	rating float(3, 2) not null,
    raters int not null,
    add_status varchar(30) not null
);
  
drop table if exists managers;

create table managers (
	manager_id int auto_increment primary key,
	email varchar(100) unique,
	first_name varchar(100) not null,
	last_name varchar(100) not null,
	password varchar(100) not null,
	rest_id int,
	phone_number varchar(20) not null,
	add_status varchar(20) not null default 'Pending'
);

drop table if exists reviews;

create table reviews(
	review_id int auto_increment primary key,
	order_id int not null unique,
    user_id int NOT NULL,
	dish_id int NOT NULL,
    courier_id int NOT NULL,
	dish_rating int default -1,
	courier_rating int default -1,
	courier_review varchar(8000) default '',
	dish_review varchar(8000) default ''
);

-------------------------------------------------------- initial admins wolvo ----------------------------------------------
insert into users values (1, 'tbabu19@freeuni.edu.ge', 'Tsotne', 'Babunashvili', 'c80adfeea5a0af6d3ab04a8dba3a8769064f0d90',
		"Admin", "Private", 'Didube', 'Dighmis Masivi V kvartali 1a','555685305');

insert into users values (2, 'tarus19@freeuni.edu.ge', 'Temur', 'Arustashvili', '5ed092a75b55d250d7cf19448ff66601d254d356', 
		"Admin", "Friends", 'Saburtalo', 'Fanjikidze str 22a/26', '595055777');
        
insert into users values (3, 'achuk19@freeuni.edu.ge', 'Akaki', 'Chukhua', 'db0d9ba0b474fc1a9ce19a389f4ed37df6350b3a',
		"Admin", "Private", 'Gldani', '3 MD Naneishvili str 20/8', '555725362');
-------------------------------------------------------- initial admins wolvo ----------------------------------------------
