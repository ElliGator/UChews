CREATE OR REPLACE FUNCTION 
	get_user_history_count(email varchar)
	RETURNS integer AS $$
	BEGIN
		RETURN (SELECT count(rating) FROM history WHERE history.user_email = $1);
	END;
	$$ LANGUAGE plpgsql;  