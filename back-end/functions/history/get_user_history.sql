CREATE OR REPLACE FUNCTION 
	get_user_history(email char) RETURNS SETOF history AS $$
		SELECT * FROM history WHERE history.user_email = email;
	$$ LANGUAGE SQL;