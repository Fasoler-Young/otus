create sequence address_SEQ start with 1 increment by 1;

create table address
(
    id   bigint not null primary key,
    street varchar(50)
);

create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50),
    addressId bigint,
    foreign key (addressId) references address (id)
);

create sequence phone_SEQ start with 1 increment by 1;

create table phone
(
    id   bigint not null primary key,
    number varchar(50),
    clientId bigint,
    foreign key (clientId) references client (id)
);

