package com.example.elli.uchews;

import org.json.JSONObject;

/**
 * Created by Chris on 2/29/2016.
 * All passwords must be SHA-1 hashed
 */
public interface UserDAO {

    /**
     *
     * @param email A user's email address
     * @param password A user's SHA-1 hashed password
     * @param fname First name
     * @param lname Last name
     * @param locality A supported FactualLocality
     * @param cuisine_stats A JSON object with (cuisine_id, int) pairs. The integer is the weight.
     * @return true if the user was added successfully. False otherwise.
     */
    public boolean addUser(String email, String password, String fname, String lname, FactualLocality locality, JSONObject cuisine_stats);

    /**
     * Lookup a user's information
     * @param email
     * @param password
     * @return a User object or null if the user cannot be found
     */
    public User getUser(String email, String password);

    /**
     *
     * @param email
     * @param password
     * @return True if the email/password combination is valid. False otherwise.
     */
    public boolean validateUser(String email, String password);

    /**
     *
     * @param email
     * @param password
     * @return True if the user was deleted successfully. False otherwise.
     */
    public boolean deleteUser(String email, String password);
}
