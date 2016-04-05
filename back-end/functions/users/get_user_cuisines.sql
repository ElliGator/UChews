CREATE OR REPLACE FUNCTION 
	get_user_cuisines(email varchar, password char)
	RETURNS json AS $user_cuisines$
	BEGIN
		RETURN (SELECT cuisine_stats FROM users WHERE users.email LIKE $1 AND users.password LIKE $2);
	END;
	$user_cuisines$ LANGUAGE plpgsql;  