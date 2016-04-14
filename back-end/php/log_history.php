#!/usr/local/bin/php
<?php

	ini_set('display_errors', 1);

	//Get arguments
	$email = $_REQUEST['email'];
	$pwd = $_REQUEST['password'];
	$rest_id = $_REQUEST['rest_id'];
	$rating = $_REQUEST['rating'];
	$timestamp = $_REQUEST['timestamp'];
	$cuisine_list = $_REQUEST['cuisines'];

	//Handle DB credentials
	$creds = file_get_contents("/cise/homes/cwhitten/UChews/back-end/credentials.json");
	$json = json_decode($creds);
	$uname = $json->{"username"};
	$password = $json->{"password"};

	//Connect to db
	$conn = pg_connect("host=postgres.cise.ufl.edu port=5432 dbname=uchews user=$uname password=$password");
	$sql = "SELECT log_history($1, $2, $3, $4);";
	$result = pg_prepare($conn, 'validate', $sql);
	$result = pg_execute($conn, 'validate', array($email, $rest_id, $rating, $timestamp));
	$row = pg_fetch_row($result);
	echo $row[0];


	//Update user's cuisine stats
	$sql = "SELECT get_user_cuisines($1, $2);";
	$result = pg_prepare($conn, 'cuisine', $sql);
	$result = pg_execute($conn, 'cuisine', array($email, $pwd));
	$row = pg_fetch_row($result);

	$user_cuisine_stats = json_decode($row[0], TRUE);
	$cuisine_list = json_decode($cuisine_list);

	if(is_null($user_cuisine_stats))
		exit();

	foreach($cuisine_list as $cuisine_id){
		if(array_key_exists($cuisine_id, $user_cuisine_stats)){
			$user_cuisine_stats[$cuisine_id] += $rating;
		}
		else {
			$user_cuisine_stats[$cuisine_id] = $rating;
		}
	}

	$json_stats = json_encode($user_cuisine_stats);

	$sql = "UPDATE users SET cuisine_stats = $1 WHERE email = $2";
	pg_prepare($conn, 'update', $sql);
	pg_execute($conn, 'update', array($json_stats, $email));

	echo pg_last_error($conn);
	echo pg_result_error($result);
	pg_close($conn);

?>