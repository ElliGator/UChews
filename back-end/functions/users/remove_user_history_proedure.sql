CREATE OR REPLACE FUNCTION remove_user_history_procedure() 
	RETURNS trigger AS $$
	BEGIN
		DELETE FROM history WHERE user_email = OLD.email;
		RETURN OLD;
	END;
	$$ LANGUAGE plpgsql;