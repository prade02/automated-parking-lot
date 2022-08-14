CREATE TABLE parking_lot (
    id serial NOT NULL,
	name VARCHAR(15) NOT NULL,
    total_floors int NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE parking_floor (
    id serial NOT NULL,
	name VARCHAR(15) NOT NULL,
    parking_lot int,
    total_slots int NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (parking_lot) REFERENCES parking_lot(id)
);

CREATE TABLE slot (
    id serial NOT NULL,
	name VARCHAR(50) NOT NULL,
    parking_floor int,
    slot_type varchar(20) NOT NULL,
    slot_status varchar(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (parking_floor) REFERENCES parking_floor(id)
);

CREATE TABLE vehicle (
    id serial NOT NULL,
    registration_number varchar(20) NOT NULL,
    vehicle_type varchar(20) NOT NULL,
    slot int NULL,
    in_time TIMESTAMP NOT NULL,
    out_time TIMESTAMP,
	fee DECIMAL,
    PRIMARY KEY (id),
    FOREIGN KEY (slot) REFERENCES slot(id)
);