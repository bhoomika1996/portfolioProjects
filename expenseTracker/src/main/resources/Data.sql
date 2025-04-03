CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY,
                                     name VARCHAR(255),
    email VARCHAR(255)
    );
CREATE TABLE IF NOT EXISTS category (
                                     id BIGINT PRIMARY KEY,
                                     name VARCHAR(255)
    );
CREATE TABLE IF NOT EXISTS expense (
                                       id BIGINT PRIMARY KEY,
                                       expensedate TIMESTAMP NOT NULL,
                                       description VARCHAR(255),
    location VARCHAR(255),
    category_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

insert into users values(1,'Bhoomika','bgajpal@yahoo.com');
insert into users values(2,'Kshitij','kc@yahoo.com');
insert into users values(3,'Lisa','lisa@yahoo.com');

insert into category values(1,'Travel');
insert into category values(2,'Auto Loan');
insert into category values(3,'Grocery');

insert into expense values(1,'2024-03-22T10:30:00.000Z', 'Vietnam trip','Vietnam',1,1);
insert into expense values(2,'2024-03-22 10:30:01', 'Vietnam trip','Vietnam',1,2);
insert into expense values(3,'2024-03-25 10:30:00', 'Curd','Hyderabad',3,1);
insert into expense values(4,'2024-03-25 10:30:10', 'Milk','Hyderabad',3,2);
insert into expense values(5,'2024-03-25 10:30:20', 'Vegetables','Hyderabad',3,2);

