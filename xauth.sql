CREATE DATABASE xauth;
USE xauth;

CREATE TABLE sys_role(
	id int primary key not null AUTO_INCREMENT,
    name varchar(100) not null,
    nameZh varchar(100) not null
);
CREATE TABLE sys_user(
	id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    username varchar(100) NOT NULL unique,
    password varchar(100) NOT NULL,
    name varchar(100),
    phone varchar(16),
    address varchar(255),
    enabled int not null default 1
);
CREATE TABLE sys_user_role(
	id int primary key not null AUTO_INCREMENT,
	uid int not null,
    rid int not null,
    CONSTRAINT fk_uid FOREIGN KEY(uid) REFERENCES sys_user(id) on delete cascade,
    CONSTRAINT fk_urid FOREIGN KEY(rid) REFERENCES sys_role(id) on delete cascade
);
CREATE TABLE sys_menu(
	id int primary key not null AUTO_INCREMENT,
    name varchar(100) not null,
    url varchar(100),
    path varchar(100),
    component varchar(100),
    iconCls varchar(100),
    parentId int
);
CREATE TABLE sys_role_menu(
	id int primary key not null AUTO_INCREMENT,
    rid int not null,
    mid int not null,
    CONSTRAINT fk_mid FOREIGN KEY(mid) REFERENCES sys_menu(id) on delete cascade,
    CONSTRAINT fk_mrid FOREIGN KEY(rid) REFERENCES sys_role(id) on delete cascade
);