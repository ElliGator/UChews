CREATE OR REPLACE FUNCTION 
	get_user(email varchar, password char)
	RETURNS json AS $user$
	BEGIN
		CREATE TEMP TABLE user_row ON COMMIT DROP AS SELECT * FROM users WHERE users.email LIKE $1 AND users.password LIKE $2 ;
		RETURN (SELECT row_to_json(user_row) FROM user_row);
	END;
	$user$ LANGUAGE plpgsql;  