CREATE OR REPLACE FUNCTION 
	get_user_neighbors(email varchar) 
	RETURNS json AS $$
	BEGIN
		CREATE TEMP TABLE user_neighbors ON COMMIT DROP AS 
			SELECT users.email, password FROM users WHERE 
				users.locality IN (SELECT locality FROM users WHERE users.email = $1) 
				AND users.email != $1;
		RETURN (SELECT array_to_json(array_agg(user_neighbors)) FROM user_neighbors);
	END;
	$$ LANGUAGE plpgsql;  