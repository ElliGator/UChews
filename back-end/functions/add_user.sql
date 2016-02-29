CREATE OR REPLACE FUNCTION 
	add_user(email varchar, password char, fname varchar, lname varchar, locality varchar, c_stats json)
	RETURNS void AS $$
	BEGIN
		INSERT INTO users (email, password, fname, lname, locality, cuisine_stats) VALUES 
			(email, password, fname, lname, locality, c_stats);
		RETURN;
	END;
	$$ LANGUAGE plpgsql;