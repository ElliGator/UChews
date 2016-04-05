CREATE OR REPLACE FUNCTION adjust_restaurant_rating() 
	RETURNS trigger AS $$
	BEGIN
		UPDATE restaurants SET rating = rating + NEW.rating WHERE restaurants.id =  NEW.restaurant_id;
		RETURN NEW;
	END;
	$$ LANGUAGE plpgsql;