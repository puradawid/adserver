CREATE TABLE ads (
    id INT PRIMARY KEY AUTO_INCREMENT,
    content VARCHAR(1000),
    client INT,
    content_category ENUM('html', 'picture'),
    category INT
);

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    password VARCHAR(255),
# PAR - partner CLI - client, ADM - admin
    credencials ENUM('PAR', 'ADM', 'CLI'),
# contact data
    first_name VARCHAR(50),
    second_name VARCHAR(50),
    email VARCHAR(120) UNIQUE,
    telephone VARCHAR(10)
);

CREATE TABLE category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20),
    parent INT
);

ALTER TABLE ads ADD CONSTRAINT client FOREIGN KEY (client) REFERENCES users(id);
ALTER TABLE ads ADD CONSTRAINT category FOREIGN KEY (category) REFERENCES category(id);

ALTER TABLE category ADD CONSTRAINT parent FOREIGN KEY (parent) REFERENCES category(id);
