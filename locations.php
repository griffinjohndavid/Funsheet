<?php

	require 'database.php';

    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        
    	// Connect to the database
    	$mysqli = ConnectToDatabase();
    	$sql = "SELECT * FROM locations";
    	$result = $mysqli->query($sql) or die("Bad query: $sql");
    
    	if ($result->num_rows == 0)
    	{
    		echo "Sorry, the database is not available.";
    	}
    	else
    	{
    	    $locations = array();
    	    while ($row = $result->fetch_assoc()) {
        		$locations[] = $row;
    	    }
    	    header('Content-Type: application/json');
            echo json_encode($locations);
    	}
    }
    else {
        header("HTTP/1.1 400 Bad Request");
        echo "Page not found.";
    }
    
?>