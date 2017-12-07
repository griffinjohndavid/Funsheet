<?php

	require 'database.php';

    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        
    	// Connect to the database
    	$mysqli = ConnectToDatabase();
    	$sql = "SELECT id, username, password FROM users";
    	$result = $mysqli->query($sql) or die("Bad query: $sql");
    
    	if ($result->num_rows == 0)
    	{
    		echo "Sorry, the database is not available.";
    	}
    	else
    	{
    	    $users = array();
    	    while ($row = $result->fetch_assoc()) {
        		$users[] = $row;
    	    }
    	    header('Content-Type: application/json');
            echo json_encode($users);
    	}
    }
    else {
        header("HTTP/1.1 400 Bad Request");
        echo "Page not found.";
    }
    
?>