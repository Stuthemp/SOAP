INSERT INTO users(login, name, password) VALUES('user1','Вася','q1Wer21');
INSERT INTO users(login, name, password) VALUES('user2','Петя','Wgr32gr');

INSERT INTO roles(id, name) VALUES(1,'Оператор');
INSERT INTO roles(id, name) VALUES(2,'Админ');
INSERT INTO roles(id, name) VALUES(3,'Аналитик');

INSERT INTO user_roles(user_id, role_id) VALUES('user1',1);
INSERT INTO user_roles(user_id, role_id) VALUES('user1',2);
INSERT INTO user_roles(user_id, role_id) VALUES('user2',1);
INSERT INTO user_roles(user_id, role_id) VALUES('user2',3);