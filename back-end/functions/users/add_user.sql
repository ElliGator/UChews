CREATE OR REPLACE FUNCTION 
	add_user(email varchar, password char, fname varchar, lname varchar, locality varchar, c_stats json)
	RETURNS boolean AS $success$
	BEGIN
		INSERT INTO users (email, password, fname, lname, locality, cuisine_stats) VALUES 
			(email, password, fname, lname, locality, c_stats);
		RETURN EXISTS(SELECT id FROM users WHERE users.email = $1 AND users.password = $2);
	END;
	$success$ LANGUAGE plpgsql;