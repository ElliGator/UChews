CREATE OR REPLACE FUNCTION 
	get_user_history(email varchar)
	RETURNS json AS $$
	BEGIN
		CREATE TEMP TABLE user_hist ON COMMIT DROP AS SELECT * FROM history WHERE history.user_email = $1;
		RETURN (SELECT array_to_json(array_agg(user_hist)) FROM user_hist);
	END;
	$$ LANGUAGE plpgsql;  