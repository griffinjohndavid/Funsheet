<?php
	require "database.php";
	
	function sortActivities($a, $b) {
	    if($a->getDistance() == $b->getDistance()){
	        return 0;
	        }
	    return ($a->getDistance() < $b->getDistance()) ? -1 : 1; 
	}
	
	$mysqli = ConnectToDatabase();
	if (isset($_GET['lat']) && isset($_GET['long'])) {
	    $lat = $mysqli->real_escape_string(trim($_GET['lat']));
	    $long = $mysqli->real_escape_string(trim($_GET['long']));
	}
	
	$sql = "SELECT activities.*, tags.tags FROM activities JOIN tags ON activities.id = tags.id";
	$result = $mysqli->query($sql) or die("Bad query: $sql");

    
	if ($result->num_rows == 0)
	{
		echo "Sorry, the database is not available.";
	} else {
	    $results = array();
	    while ($row = $result->fetch_assoc()) {
    		$results[] = $row;
	    }
 	    header('Content-Type: application/json');
 	    $activities = array();
 	    for ($i = 0; $i < count($results); $i++) {
 	        $id = $results[$i]["id"];
 	        $sql = "SELECT * FROM reviews WHERE id = '$id'";
	        $result = $mysqli->query($sql) or die("Bad query: $sql");
	        while ($row = $result->fetch_assoc()) {
        		$reviews[] = $row;
    	    }
    	    $rating = 0;
    	    $reviewCount = 0;
    	    for ($j = 0; $j < count($reviews); $j++) {
    	        $rating += $reviews[$j]["rating"];
    	        $reviewCount++;
    	    }
    	    $rating /= $reviewCount;
 	        
 	        $distance = calculateDistance($lat, $long, $results[$i]["latitude"], $results[$i]["longitude"]);
 	        $activity = new Activity();
 	        $activity->setId($results[$i]["id"]);
 	        $activity->setName($results[$i]["name"]);
 	        $activity->setDescription($results[$i]["description"]);
 	        $activity->setLatitude($results[$i]["latitude"]);
 	        $activity->setLongitude($results[$i]["longitude"]);
 	        $activity->setDistance($distance);
			$activity->setLocation($results[$i]["location"]);
			$activity->setTags($results[$i]["tags"]);
			$activity->setPrice($results[$i]["price"]);
			$activity->setRating($rating);
			$activity->setReviewCount($reviewCount);
			$activities[$i] = $activity;
 	      //  
 	    }
 	    usort($activities, "sortActivities");
 	     echo "[";
 	    for ($i = 0; $i < count($activities); $i++) {
           echo "{\"id\": " . $activities[$i]->getId() . ",\"name\": \""
                . $activities[$i]->getName() . "\",\"description\": \""
                . $activities[$i]->getDescription() . "\",\"latitude\": "
                . $activities[$i]->getLatitude() . ",\"longitude\": "
                . $activities[$i]->getLongitude() . ",\"distance\": "
                . $activities[$i]->getDistance() . ",\"location\": \""
                . $activities[$i]->getLocation() . "\",\"tags\": \""
                . $activities[$i]->getTags() . "\",\"rating\": "
                . $activities[$i]->getRating() . ",\"reviewCount\": "
                . $activities[$i]->getReviewCount() . ",\"price\": "
                . $activities[$i]->getPrice() . "}";
 	        if ($i < count($results) - 1) {
 	            echo ",";
 	        }
 	    }
 	    echo "]";
	}
?>