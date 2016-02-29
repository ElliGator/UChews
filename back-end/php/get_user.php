#!/usr/local/bin/php
<?php

	ini_set('display_errors', 1);

	//Get arguments
	$email = $_REQUEST['email'];
	$pwd = $_REQUEST['password'];

	//Handle DB credentials
	$creds = file_get_contents("/cise/homes/cwhitten/UChews/back-end/credentials.json");
	$json = json_decode($creds);
	$uname = $json->{"username"};
	$password = $json->{"password"};

	//Connect to db
	$conn = pg_connect("host=postgres.cise.ufl.edu port=5432 dbname=uchews user=$uname password=$password");
	$sql = "SELECT get_user($1, $2);";
	$result = pg_prepare($conn, 'validate', $sql);
	$result = pg_execute($conn, 'validate', array($email, $pwd));
	$row = pg_fetch_row($result);
	echo $row[0];

	//echo pg_last_error($conn);
	//echo pg_result_error($result);

?>