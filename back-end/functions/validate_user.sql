CREATE OR REPLACE FUNCTION 
	validate_user(email varchar, password char)
	RETURNS boolean AS $success$
	BEGIN
		RETURN EXISTS(SELECT id FROM users WHERE users.email LIKE $1 AND users.password LIKE $2);
	END;
	$success$ LANGUAGE plpgsql;