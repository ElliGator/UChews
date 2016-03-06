DROP TABLE history;

CREATE TABLE history (
	user_email		VARCHAR(40) REFERENCES users(email),
	restaurant_id   CHAR(36) REFERENCES restaurants(id),
	rating			integer,
	date_made 		timestamp
);