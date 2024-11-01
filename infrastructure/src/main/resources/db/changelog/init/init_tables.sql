--liquibase formatted sql

-- changeset m-silantev:1
CREATE TABLE order_status (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- changeset m-silantev:2
CREATE TABLE courier_status (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- changeset m-silantev:3
CREATE TABLE transport (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    speed INT NOT NULL
);

-- changeset m-silantev:4
CREATE TABLE courier_aggregate (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location_x INT NOT NULL,
    location_y INT NOT NULL,
    status_id INT NOT NULL,
    transport_id INT NOT NULL,

    CONSTRAINT fk_status FOREIGN KEY (status_id) REFERENCES courier_status(id),
    CONSTRAINT fk_transport FOREIGN KEY (transport_id) REFERENCES transport(id)
);

-- changeset m-silantev:5
CREATE TABLE order_aggregate (
    id VARCHAR(255) PRIMARY KEY,
    location_x INT NOT NULL,
    location_y INT NOT NULL,
    status_id INT NOT NULL,
    courier_id VARCHAR(255),
    CONSTRAINT fk_status FOREIGN KEY (status_id) REFERENCES order_status(id),
    CONSTRAINT fk_courier FOREIGN KEY (courier_id) REFERENCES courier_aggregate(id)
);

-- changeset m-silantev:6
INSERT INTO courier_status (id, name) VALUES (1, 'FREE');
INSERT INTO courier_status (id, name) VALUES (2, 'BUSY');

-- changeset m-silantev:7
INSERT INTO order_status (id, name) VALUES (1, 'CREATED');
INSERT INTO order_status (id, name) VALUES (2, 'ASSIGNED');
INSERT INTO order_status (id, name) VALUES (3, 'COMPLETED');

-- changeset m-silantev:8
INSERT INTO transport (id, name, speed) VALUES (1, 'PEDESTRIAN', 1);
INSERT INTO transport (id, name, speed) VALUES (2, 'BICYCLE', 2);
INSERT INTO transport (id, name, speed) VALUES (3, 'CAR', 3);
