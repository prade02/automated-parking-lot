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

ALTER TABLE parking_floor
ADD COLUMN name VARCHAR(15) NOT NULL AFTER id;

CREATE TABLE slot (
    id int NOT NULL AUTO_INCREMENT,
    parking_floor int,
    slot_type varchar(20) NOT NULL,
    slot_status varchar(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (parking_floor) REFERENCES parking_floor(id)
);

ALTER TABLE slot
ADD COLUMN name VARCHAR(15) NOT NULL AFTER id;

ALTER TABLE slot MODIFY COLUMN name varchar(50);

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

ALTER TABLE vehicle ADD COLUMN fee DECIMAL;

ALTER TABLE vehicle DROP COLUMN vehicle_color;

DELIMITER $$
CREATE PROCEDURE `select_available_slot` (IN lot_id int, IN type VARCHAR(20), OUT slot_id INT)
BEGIN
	SELECT s.id INTO slot_id FROM slot s INNER JOIN parking_floor f 
	ON s.parking_floor = f.id 
	WHERE f.parking_lot = lot_id 
	AND s.slot_status = 'VACANT'
	AND s.slot_type = type
	ORDER BY s.name asc
	LIMIT 1;
END $$