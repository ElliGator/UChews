CREATE OR REPLACE FUNCTION increment_restaurant_rating(rest_id char) 
	RETURNS boolean AS $success$
	BEGIN
		UPDATE restaurants SET rating = rating + 1 WHERE restaurants.id =  rest_id;
		RETURN TRUE;
	END;
	$success$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION increment_restaurant_rating(rest_id char, amount int) 
	RETURNS boolean AS $success$
	BEGIN
		UPDATE restaurants SET rating = rating + amount WHERE restaurants.id ==  rest_id;
		RETURN TRUE;
	END;
	$success$ LANGUAGE plpgsql;