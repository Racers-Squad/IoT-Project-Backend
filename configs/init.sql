SET search_path TO public;

CREATE TABLE history (
    id          serial PRIMARY KEY,
    carNumber   varchar(9) NOT NULL,
    user_id     integer NOT NULL,
    start_time  timestamp NOT NULL,
    end_time    timestamp NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create User table
CREATE TABLE Users (
                       ID SERIAL PRIMARY KEY,
                       Name VARCHAR(50),
                       Surname VARCHAR(50),
                       Phone VARCHAR(20),
                       Email VARCHAR(100),
                       Password VARCHAR(100),
                       isAdmin boolean
);

-- Create Driver table
CREATE TABLE Driver (
                        ID SERIAL PRIMARY KEY,
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
