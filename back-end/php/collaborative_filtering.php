#!/usr/local/bin/php
<?php

	ob_start();
	require 'get_user_history.php';
	ob_end_clean();

	class User {
		public $email = "";
		public $password = "";
	}

	$DEFAULT_CUISINE_RATING = 0;

	//Returns a list of restaurant ids
	function selectUsingCollabFiltering($user_email, $user_password){
 		$closestNeighbor = getClosestNeighbor($user_email, $user_password);
 		$neighborHistory = getUserAggHistory($closestNeighbor->email);
 		return $neighborHistory;
	}

	//Returns 
	function getClosestNeighbor($user_email, $user_password){
		$conn = openBackendConnection();

		//Check if user is in cold start phase
		$sql = "SELECT get_user_neighbors($1);";
		$result = pg_prepare($conn, 'neighbors', $sql);
		$result = pg_execute($conn, 'neighbors', array($user_email));
		$row = pg_fetch_row($result);
		$user_neighbors = json_decode($row[0], TRUE);
		$user = new User();
		$user->email = $user_email;
		$user->password = $user_password;

		$closest_Neighbor = new User();
		$minAngle = 360;
		foreach($user_neighbors as $neighbor){
			$neighborObj = new User();
			$neighborObj->email = $neighbor["email"];
			$neighborObj->password = $neighbor["password"];
			
			$angleBetweenNeighbor = compareUsersOnCuisine($user, $neighborObj);

			if($angleBetweenNeighbor < $minAngle)
			{
				$minAngle = $angleBetweenNeighbor;
				$closest_Neighbor = $neighborObj;
			}
		}

		return $closest_Neighbor;
	}

	//$u1 and $u2 should be json objects containing email and pwd
	//Returns the angle between them representing their closeness
	function compareUsersOnCuisine($u1, $u2){
		$u1_email = $u1->email;
		$u1_password = $u1->password;
		$u2_email = $u2->email;
		$u2_password = $u2->password;
		
		$u1_vector = vectorizeUserAsCuisineStats($u1_email, $u1_password);
		$u2_vector = vectorizeUserAsCuisineStats($u2_email, $u2_password);

		return calculateAngleBetweenVectors($u1_vector, $u2_vector, $GLOBALS['DEFAULT_CUISINE_RATING']);
	}

	//$v1 and $v2 must be associative arrays
	//The keys in $v1 will be iterated over
	function calculateAngleBetweenVectors($v1, $v2, $fillVal){
		$v1Keys = array_keys($v1);
		$dotProduct = 0;
		$v1Mag = 0;
		$v2Mag = 0;
		foreach($v1Keys as $key){
			if(array_key_exists($key, $v2) == FALSE){
				$v2[$key] = $fillVal;
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

		$sql = "SELECT get_user_cuisines($1, $2);";
		$result = pg_prepare($conn, 'cuisine', $sql);
		$result = pg_execute($conn, 'cuisine', array($user_email, $user_pwd));
		$row = pg_fetch_row($result);
		$user_cuisine_stats = json_decode($row[0], TRUE);
		pg_close($conn);

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

	//$user1 = vectorizeUserAsCuisineStats('tubby', '7b1f1ad7032ee95157646782ca3db19c30737e9d');
	//$user2 = vectorizeUserAsCuisineStats('cwhitten@ufl.edu', 'eda405cc8daadace0151fefa92d6ba7ddf1bb598');
	//echo "Angle: ".calculateAngleBetweenVectors($user1, $user2, $GLOBALS['DEFAULT_CUISINE_RATING']);

	//echo selectUsingCollabFiltering('tubby', '7b1f1ad7032ee95157646782ca3db19c30737e9d');
?>