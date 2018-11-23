create table STOCK (
    ID uuid not null,
    PRODUCT uuid not null,
    BRANCH uuid not null,
    NUMBER_OF_ITEMS int not null,
    CONSTRAINT UC_STOCK UNIQUE (PRODUCT,BRANCH)
);

