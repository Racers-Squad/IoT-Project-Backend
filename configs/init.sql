SET search_path TO public;

-- Create User table
CREATE TABLE IF NOT EXISTS Users (
                       ID INT PRIMARY KEY,
                       Name VARCHAR(50),
                       Surname VARCHAR(50),
                       Phone VARCHAR(20),
                       Email VARCHAR(100),
                       Password VARCHAR(100),
                       isAdmin boolean
);

-- Create Driver table
CREATE TABLE IF NOT EXISTS  Driver (
                        ID INT PRIMARY KEY,
                        UserID INT REFERENCES Users(ID),
                        DriverLicenseNumber VARCHAR(20)
);

-- Create Car table
CREATE TABLE IF NOT EXISTS  Car (
                     id VARCHAR(15) PRIMARY KEY ,
                     carBrand VARCHAR(50),
                     Model VARCHAR(50),
                     Year INT,
                     Status INT
);

-- Create Reservation table
CREATE TABLE IF NOT EXISTS  Reservation (
                             ID INT PRIMARY KEY,
                             DriverID INT REFERENCES Driver(ID),
                             CarID varchar(15) REFERENCES Car(ID),
                             StartTime TIMESTAMP,
                             EndTime TIMESTAMP
);

-- Create Trip table
CREATE TABLE IF NOT EXISTS  Trip (
                      ID INT PRIMARY KEY,
                      DriverID INT REFERENCES Driver(ID),
                      CarID varchar(15) REFERENCES Car(ID),
                      StartLocation VARCHAR(100),
                      EndLocation VARCHAR(100),
                      StartTime TIMESTAMP,
                      EndTime TIMESTAMP
);

CREATE SEQUENCE IF NOT EXISTS  user_id_sequense INCREMENT 1 START 1;
CREATE SEQUENCE IF NOT EXISTS  int_id_sequense INCREMENT 1 START 2;
CREATE SEQUENCE IF NOT EXISTS  reservation_id_sequense INCREMENT 1 START 1;
CREATE SEQUENCE IF NOT EXISTS trip_id_sequense INCREMENT 1 START 1;


INSERT INTO Users VALUES
(1, 'Admin', 'Admin', '+7 800 555-35-35', 'admin', '21232f297a57a5a743894a0e4a801fc3', 1);