#!/usr/local/bin/php
<?php

	//ini_set('display_errors', 1);

	//Get arguments
	$email = $_REQUEST['email'];

	echo getUserHistory($email);
	
	

	function getUserHistory($user_email){
		//Handle DB credentials
		$creds = file_get_contents("/cise/homes/cwhitten/UChews/back-end/credentials.json");
		$json = json_decode($creds);
		$uname = $json->{"username"};
		$password = $json->{"password"};

		//Connect to db
		$conn = pg_connect("host=postgres.cise.ufl.edu port=5432 dbname=uchews user=$uname password=$password");
		$sql = "SELECT get_user_history($1);";
		$result = pg_prepare($conn, 'full_hist', $sql);
		$result = pg_execute($conn, 'full_hist', array($user_email));
		$row = pg_fetch_row($result);

		echo pg_last_error($conn);
		echo pg_result_error($result);
		pg_close($conn);

		return $row[0];
	}

	function getUserAggHistory($user_email){
		//Handle DB credentials
		$creds = file_get_contents("/cise/homes/cwhitten/UChews/back-end/credentials.json");
		$json = json_decode($creds);
		$uname = $json->{"username"};
		$password = $json->{"password"};

		//Connect to db
		$conn = pg_connect("host=postgres.cise.ufl.edu port=5432 dbname=uchews user=$uname password=$password");
		$sql = "SELECT get_user_agg_history($1);";
		$result = pg_prepare($conn, 'agg_hist', $sql);
		$result = pg_execute($conn, 'agg_hist', array($user_email));
		$row = pg_fetch_row($result);

		echo pg_last_error($conn);
		echo pg_result_error($result);
		pg_close($conn);

		return $row[0];
	}

?>