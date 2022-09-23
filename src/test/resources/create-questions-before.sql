delete from roles;
delete from users;
delete from user_roles;

INSERT INTO users(login, name, password) VALUES('userTest1','Вася','q1Wer21');
INSERT INTO users(login, name, password) VALUES('userTest2','Петя','Wgr32gr');

INSERT INTO roles(id, name) VALUES(1,'Оператор');
INSERT INTO roles(id, name) VALUES(2,'Админ');
INSERT INTO roles(id, name) VALUES(3,'Аналитик');

INSERT INTO user_roles(user_id, role_id) VALUES('userTest1',1);
INSERT INTO user_roles(user_id, role_id) VALUES('userTest1',2);
INSERT INTO user_roles(user_id, role_id) VALUES('userTest2',1);
INSERT INTO user_roles(user_id, role_id) VALUES('userTest2',3);
