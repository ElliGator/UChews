CREATE OR REPLACE FUNCTION 
	log_history(email char, rest_id char, rating integer, d timestamp) 
	RETURNS boolean AS $success$
	BEGIN
		INSERT INTO history VALUES 
			(email, rest_id, rating, d);
		RETURN EXISTS
			(SELECT * FROM history WHERE history.user_email = $1 AND history.restaurant_id = $2 AND history.date_made = $4);
	END;
	$success$ LANGUAGE plpgsql;