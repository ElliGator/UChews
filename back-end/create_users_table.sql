DROP TABLE users;

CREATE TABLE users (
	id					SERIAL PRIMARY KEY,
	email				varchar(40) UNIQUE,
	password			char(40),
	first				varchar(20),
	last				varchar(20),
	locality			varchar(30),
	cuisine_stats		json
);