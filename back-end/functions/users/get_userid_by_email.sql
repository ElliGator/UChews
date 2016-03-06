CREATE OR REPLACE FUNCTION 
	get_userid_by_email(email varchar)
	RETURNS integer AS $userid$
	BEGIN
		RETURN (SELECT id FROM users WHERE users.email = $1);
	END;
	$userid$ LANGUAGE plpgsql;  