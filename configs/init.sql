SET search_path TO public;

-- Create User table
CREATE TABLE Users (
                       ID INT PRIMARY KEY,
                       Name VARCHAR(50),
                       Surname VARCHAR(50),
                       Phone VARCHAR(20),
                       Email VARCHAR(100),
                       Password VARCHAR(100),
                       isAdmin boolean
);

-- Create Driver table
CREATE TABLE Driver (
                        ID INT PRIMARY KEY,
                        UserID INT REFERENCES Users(ID),
                        DriverLicenseNumber VARCHAR(20)
);

-- Create Car table
CREATE TABLE Car (
                     id VARCHAR(15),
                     carBrand VARCHAR(50),
                     Model VARCHAR(50),
                     Year INT,
                     Status INT
);

-- Create Reservation table
CREATE TABLE Reservation (
                             ID SERIAL PRIMARY KEY,
                             DriverID INT REFERENCES Driver(ID),
                             CarID varchar(15) REFERENCES Car(ID),
                             StartTime TIMESTAMP,
                             EndTime TIMESTAMP
);

-- Create Trip table
CREATE TABLE Trip (
                      ID SERIAL PRIMARY KEY,
                      DriverID INT REFERENCES Driver(ID),
                      CarID varchar(15) REFERENCES Car(ID),
                      StartLocation VARCHAR(100),
                      EndLocation VARCHAR(100),
                      StartTime TIMESTAMP,
                      EndTime TIMESTAMP
);

CREATE SEQUENCE user_id_sequense INCREMENT 1 START 1;
CREATE SEQUENCE int_id_sequense INCREMENT 1 START 1;
CREATE SEQUENCE reservation_id_sequense INCREMENT 1 START 1;
CREATE SEQUENCE trip_id_sequense INCREMENT 1 START 1;


INSERT INTO Users VALUES
(nextval(user_id_sequense), 'Admin', 'Admin', '+7 800 555-35-35', 'admin', 'admin', 1);