#!/usr/local/bin/php
<?php

	$DEFAULT_CUISINE_RATING = 0;

	//Returns a list of restaurant ids
	function selectUsingCollabFiltering($user_email){

	}

	function getClosestNeighbor(){

	}

	//$v1 and $v2 must be associative arrays
	//The keys in $v1 will be iterated over
	function calculateAngleBetweenVectors($v1, $v2){
		$v1Keys = array_keys($v1);
		$dotProduct = 0;
		$v1Mag = 0;
		$v2Mag = 0;
		foreach($v1Keys as $key){
			if(array_key_exists($key, $v2) == FALSE){
				$v2[$key] = $DEFAULT_CUISINE_RATING;
			}
			
			$dotProduct += $v1[$key] * $v2[$key];
			$v1Mag += pow($v1[$key], 2);
			$v2Mag += pow($v2[$key], 2);
		}

		unset($key);
		$v1Mag = sqrt($v1Mag);
		$v2Mag = sqrt($v2Mag);
		$cosineVal = $dotProduct / ($v1Mag * $v2Mag);
		$angleVal = acos($cosineVal);
		return abs($angleVal);
	}


	//Returns a user represented an associative array of cuisine id => cuisine rating
	function vectorizeUserAsCuisineStats($user_email, $user_pwd){
		$conn = openBackendConnection();

		//Check if user is in cold start phase
		$sql = "SELECT get_user_cuisines($1, $2);";
		$result = pg_prepare($conn, 'cuisine', $sql);
		$result = pg_execute($conn, 'cuisine', array($user_email, $user_pwd));
		$row = pg_fetch_row($result);
		$user_cuisine_stats = json_decode($row[0], TRUE);
		return $user_cuisine_stats;
	}

	function openBackendConnection(){
		//Handle DB credentials
		$creds = file_get_contents("/cise/homes/cwhitten/UChews/back-end/credentials.json");
		$json = json_decode($creds);
		$uname = $json->{"username"};
		$password = $json->{"password"};

		//Connect to db
		$conn = pg_connect("host=postgres.cise.ufl.edu port=5432 dbname=uchews user=$uname password=$password");
		return $conn;
	}

	$user1 = vectorizeUserAsCuisineStats('tubby', '7b1f1ad7032ee95157646782ca3db19c30737e9d');
	$user2 = vectorizeUserAsCuisineStats('testUser', 'cc777c325fe5d7f39fc1bb44e1098dff09c88112');
	echo "Angle: ".calculateAngleBetweenVectors($user1, $user2);
?>