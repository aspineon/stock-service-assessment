create table RESERVED_STOCK (
    ID uuid not null,
    PRODUCT uuid not null,
    BRANCH uuid not null,
    NUMBER_OF_ITEMS int not null,
    EXPIRES timestamp not null,

    CREATED_BY varchar(100) not null,
    CREATED_DATE timestamp not null,
    MODIFIED_BY varchar(100),
    MODIFIED_DATE timestamp,

    CONSTRAINT UC_RESERVED_STOCK UNIQUE (PRODUCT,BRANCH)
);
