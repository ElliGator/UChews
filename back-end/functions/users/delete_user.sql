CREATE OR REPLACE FUNCTION 
	delete_user(email varchar, password char)
	RETURNS boolean AS $success$
	BEGIN
		DELETE FROM users WHERE users.email = $1 AND users.password = $2;
		RETURN NOT EXISTS(SELECT id FROM users WHERE users.email = $1 AND users.password = $2);
	END;
	$success$ LANGUAGE plpgsql;