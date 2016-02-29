DROP TABLE restaurants;

CREATE TABLE restaurants (
	id				CHAR(36) PRIMARY KEY,
	locality		VARCHAR(30),
	region			VARCHAR(5),
	rating			integer
);