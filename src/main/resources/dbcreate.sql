CREATE TABLE users(id SERIAL PRIMARY KEY,
                   username VARCHAR(30) UNIQUE,
                   fio VARCHAR(254));

CREATE TABLE logins(id SERIAL PRIMARY KEY,
                    access_date TIMESTAMP,
                    user_id INT FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
		    application VARCHAR(30));

ALTER TABLE logins ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;
