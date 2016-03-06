DROP TABLE users;

CREATE TABLE users (
	id					SERIAL PRIMARY KEY,
	email				varchar(40) UNIQUE,
	password			char(40),
	fname				varchar(20),
	lname				varchar(20),
	locality			varchar(30),
	cuisine_stats		json
);