create table account
(
    id    integer auto_increment,
    name  varchar(256) not null,
    email varchar(256) not null,
    primary key (id)
);

create table shop_user
(
    id        integer auto_increment,
    user_name varchar(256) not null,
    email     varchar(256) not null,
    primary key (id)
);