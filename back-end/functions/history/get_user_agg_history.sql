CREATE OR REPLACE FUNCTION 
	get_user_agg_history(email varchar)
	RETURNS json AS $$
	BEGIN
		CREATE TEMP TABLE user_hist ON COMMIT DROP AS 
			SELECT restaurant_id AS id, sum(rating) AS total_rating 
				FROM history 
				WHERE user_email = $1 
				GROUP BY restaurant_id 
				ORDER BY total_rating DESC;
		RETURN (SELECT array_to_json(array_agg(user_hist)) FROM user_hist);
	END;
	$$ LANGUAGE plpgsql;  