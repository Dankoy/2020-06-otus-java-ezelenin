insert into TEST.PUBLIC.TUSER(AGE, NAME)
VALUES (14, 'user1');
insert into TEST.PUBLIC.TUSER(AGE, NAME)
VALUES (12, 'user2');
insert into TEST.PUBLIC.TUSER(AGE, NAME)
VALUES (18, 'user3');

insert into TEST.PUBLIC.TADDRESS(STREET, USER_ID)
VALUES ('street1', 1);
insert into TEST.PUBLIC.TADDRESS(STREET, USER_ID)
VALUES ('street2', 2);
insert into TEST.PUBLIC.TADDRESS(STREET, USER_ID)
VALUES ('street3', 3);


insert into TEST.PUBLIC.TPHONES(ID, NUMBER, USER_ID)
VALUES (1, 'number1', 1);
insert into TEST.PUBLIC.TPHONES(ID, NUMBER, USER_ID)
VALUES (2, 'number2', 1);
insert into TEST.PUBLIC.TPHONES(ID, NUMBER, USER_ID)
VALUES (3, 'number3', 1);
insert into TEST.PUBLIC.TPHONES(ID, NUMBER, USER_ID)
VALUES (4, 'number4', 1);

insert into TEST.PUBLIC.TPHONES(ID, NUMBER, USER_ID)
VALUES (5, 'number1', 2);
insert into TEST.PUBLIC.TPHONES(ID, NUMBER, USER_ID)
VALUES (6, 'number2', 2);
insert into TEST.PUBLIC.TPHONES(ID, NUMBER, USER_ID)
VALUES (7, 'number3', 2);
insert into TEST.PUBLIC.TPHONES(ID, NUMBER, USER_ID)
VALUES (8, 'number4', 2);

insert into TEST.PUBLIC.TPHONES(ID, NUMBER, USER_ID)
VALUES (9, 'number1', 3);
insert into TEST.PUBLIC.TPHONES(ID, NUMBER, USER_ID)
VALUES (10, 'number2', 3);
insert into TEST.PUBLIC.TPHONES(ID, NUMBER, USER_ID)
VALUES (11, 'number3', 3);
insert into TEST.PUBLIC.TPHONES(ID, NUMBER, USER_ID)
VALUES (12, 'number4', 3);
