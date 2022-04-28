use automated_parking_lot;

CREATE TABLE parking_lot (
    id int NOT NULL AUTO_INCREMENT,
    total_floors int NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE parking_lot
ADD COLUMN name VARCHAR(15) NOT NULL AFTER id;

CREATE TABLE parking_floor (
    id int NOT NULL AUTO_INCREMENT,
    parking_lot int,
    total_slots int NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (parking_lot) REFERENCES parking_lot(id)
);

CREATE TABLE slot (
    id int NOT NULL AUTO_INCREMENT,
    parking_floor int,
    slot_type varchar(20) NOT NULL,
    slot_status varchar(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (parking_floor) REFERENCES parking_floor(id)
);

CREATE TABLE vehicle (
    id int NOT NULL AUTO_INCREMENT,
    registration_number varchar(20) NOT NULL,
    vehicle_color varchar(15) NOT NULL,
    vehicle_type varchar(20) NOT NULL,
    slot int NULL,
    in_time DATETIME NOT NULL,
    out_time DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (slot) REFERENCES slot(id)
);