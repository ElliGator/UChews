#!/usr/local/bin/php
<?php

	ini_set('display_errors', 1);

	//Get arguments
	$email = $_REQUEST['email'];
	$pwd = $_REQUEST['password'];
	$fname = $_REQUEST['fname'];
	$lname = $_REQUEST['lname'];
	$locality = $_REQUEST['locality'];
	$cuisines = $_REQUEST['cuisines'];
	//$cuisines = '{"312":"1"}';

	//Handle DB credentials
	$creds = file_get_contents("/cise/homes/cwhitten/UChews/back-end/credentials.json");
	$json = json_decode($creds);
	$uname = $json->{"username"};
	$password = $json->{"password"};

	//Connect to db
	$conn = pg_connect("host=postgres.cise.ufl.edu port=5432 dbname=uchews user=$uname password=$password");
	$sql = "SELECT add_user($1, $2, $3, $4, $5, $6);";
	$result = pg_prepare($conn, 'validate', $sql);
	$result = pg_execute($conn, 'validate', array($email, $pwd, $fname, $lname, $locality, $cuisines));
	$row = pg_fetch_row($result);
	echo $row[0];
	
	echo pg_last_error($conn);
	echo pg_result_error($result);

?>