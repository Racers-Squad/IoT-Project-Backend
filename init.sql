SET search_path TO public;

CREATE TABLE users
(
    id       serial PRIMARY KEY,
    email    varchar(255) UNIQUE NOT NULL,
    password varchar(255)        NOT NULL,
    admin_rights boolean
);

CREATE TABLE history (
    id          serial PRIMARY KEY,
    carNumber   varchar(9) NOT NULL,
    user_id     integer NOT NULL,
    start_time  timestamp NOT NULL,
    end_time    timestamp NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
