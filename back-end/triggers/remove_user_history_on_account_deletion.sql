DROP TRIGGER remove_user_history_on_account_deletion ON users;

CREATE TRIGGER remove_user_history_on_account_deletion
	BEFORE DELETE ON users
	FOR EACH ROW
	EXECUTE PROCEDURE remove_user_history_procedure() ;