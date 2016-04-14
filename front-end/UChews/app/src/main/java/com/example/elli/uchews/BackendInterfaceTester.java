package com.example.elli.uchews;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

/**
 * Created by Chris on 4/4/2016.
 */
public class BackendInterfaceTester {

    public void testIndividualSelect(){
        new AsyncTask<Void, Void, Integer>(){

            @Override
            protected Integer doInBackground(Void... params) {
                User user = new User("testUser", "2f61f9b9822dd99b61efc4e22b3d6754edc0defa", "Daquan", "FromDaTrap", FactualLocality.GAINESVILLE, null);
                return new RestaurantSelector().individualSelect(user).size();
            }

            @Override
            protected void onPostExecute(Integer b) {
                Log.d("Backend Test", "IndividualSelect() response: " + b.toString());
            }
        }.execute();
    }
    public void testAddUser(){
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {
                return new StandardUserDao().addUser("testUser", "2f61f9b9822dd99b61efc4e22b3d6754edc0defa", "Daquan", "FromDaTrap", FactualLocality.GAINESVILLE, Cuisine.BBQ, Cuisine.BURGERS);
            }

            @Override
            protected void onPostExecute(Boolean b) {
                Log.d("Backend Test", "AddUser() response: " + b.toString());
            }
        }.execute();
    }

    public void testValidateUser(){
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {
                return new StandardUserDao().validateUser("testUser", "2f61f9b9822dd99b61efc4e22b3d6754edc0defa");
            }

            @Override
            protected void onPostExecute(Boolean b) {
                Log.d("Backend Test", "ValidateUser() response: " + b.toString());
            }
        }.execute();
    }

    public void testDeleteUser(){
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {
                return new StandardUserDao().deleteUser("testUser", "2f61f9b9822dd99b61efc4e22b3d6754edc0defa");
            }

            @Override
            protected void onPostExecute(Boolean b) {
                Log.d("Backend Test", "DeleteUser() response: " + b.toString());
            }
        }.execute();
    }

    public void testGetUser(){
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {
                return (new StandardUserDao().getUser("testUser", "2f61f9b9822dd99b61efc4e22b3d6754edc0defa")) != null ;
            }

            @Override
            protected void onPostExecute(Boolean b) {
                Log.d("Backend Test", "GetUser() response: " + b.toString());
            }
        }.execute();
    }

    public void testLogHistory(){
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {
                JSONArray cuisines = new JSONArray();
                cuisines.put(358);
                Restaurant r = new Restaurant("79111919-65e1-4744-b039-c8541ad9d772", "Olive Garden", "I don't care", FactualLocality.GAINESVILLE.getName(), FactualRegion.FLORIDA.getName(), "32612", 0.0, 0.0, cuisines, null, null);
                return new StandardUserDao().logHistory("testUser","cc777c325fe5d7f39fc1bb44e1098dff09c88112", r, Rating.POSITIVE);
            }

            @Override
            protected void onPostExecute(Boolean b) {
                Log.d("Backend Test", "LogHistory() response: " + b.toString());
            }
        }.execute();
    }
}
