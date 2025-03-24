DROP TABLE IF EXISTS habit_user;
CREATE TABLE habit_user(
   id UUID PRIMARY KEY,
   email VARCHAR(255) NOT NULL UNIQUE,
   username VARCHAR(30) NOT NULL UNIQUE,
   first_name VARCHAR(30) DEFAULT 'unspecified',
   last_name VARCHAR(50) DEFAULT 'unspecified' ,
   age INTEGER DEFAULT 0
);



DROP TABLE IF EXISTS habit;
CREATE TABLE habit(
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    habit_frequency VARCHAR(255) NOT NULL,
    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    end_date TIMESTAMP DEFAULT DATEADD(YEAR, 1, CURRENT_TIMESTAMP()),
    reminder BOOLEAN DEFAULT FALSE,
    user_id UUID
);

ALTER TABLE habit
ADD FOREIGN KEY (user_id) REFERENCES habit_user(id);

