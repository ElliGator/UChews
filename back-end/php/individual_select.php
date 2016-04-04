#!/usr/local/bin/php
<?php

	ini_set('display_errors', 1);
	$COLD_START_THRESHOLD = 10;
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
	
	//Check if user is in cold start phase
	$sql = "SELECT get_user_history_count($1);";
	$result = pg_prepare($conn, 'count', $sql);
	$result = pg_execute($conn, 'count', array($email));
	$row = pg_fetch_row($result);
	$user_history_count = $row[0];

	if($user_history_count < $COLD_START_THRESHOLD)
	{
		//Get user cuisine stats
		$sql = "SELECT get_user_cuisines($1, $2);";
		$result = pg_prepare($conn, 'cuisines', $sql);
		$result = pg_execute($conn, 'cuisines', array($email, $pwd));
		$row = pg_fetch_row($result);
		$user_cuisines = $row[0];

		$response = array("isInColdStart" => "true", "userCuisines" => $user_cuisines);
		$response = stripslashes(json_encode($response));

		echo $response;
	}
	else
	{
		//Run collab filtering algorithm
	}

	echo pg_last_error($conn);
	echo pg_result_error($result);

?>