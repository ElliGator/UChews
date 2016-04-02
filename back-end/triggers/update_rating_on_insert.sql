DROP TRIGGER update_rating_on_insert ON history;

CREATE TRIGGER update_rating_on_insert
	AFTER INSERT ON history
	FOR EACH ROW
	EXECUTE PROCEDURE adjust_restaurant_rating();