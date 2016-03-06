DROP TABLE history;

CREATE TABLE history (
	user_id			integer REFERENCES users(id),
	restaurant_id   CHAR(36) REFERENCES restaurants(id),
	rating			integer,
	date_made 		timestamp
);