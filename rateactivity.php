<?php 
	require "database.php";

    if ($_SERVER['REQUEST_METHOD'] == "POST") {
    	if (isset($_POST['id']) && isset($_POST['username']) && isset($_POST['rating'])) {
    	    $mysqli = ConnectToDatabase();
    		$id = $mysqli->real_escape_string(trim($_POST['id']));
    	    $username = $mysqli->real_escape_string(trim($_POST['username']));
    	    $rating = $mysqli->real_escape_string(trim($_POST['rating']));
    
    		$sql = "SELECT COUNT(*) FROM reviews WHERE id = $id AND user = '$username'";
    		$result = $mysqli->query($sql) or die("Bad query: $sql");
    		$row = $result->fetch_row();
    		if ($row[0] == 0) {
    		    echo "Num_Rows = 0";
    			$sql = "INSERT INTO reviews VALUES ($id,'$username',$rating)";
    			echo $sql . "\n";
    			$result = $mysqli->query($sql) or die("Bad query: $sql");
    			echo $result->num_rows;
    			if ($result->num_rows == 1) {
    				$output = "SUCCESS_REVIEW_SAVED";
    			} else {
    				$output = "ERROR_NOT_ADDED";
    			}
         	} else {
    			$output = "SUCCESS_REVIEW_SAVED";
    		}
	    } else {
	        echo $_POST['id'];
			echo $_POST['username'];
			echo $_POST['rating'];
	    }
	} else {
		$output = "ERROR_SERVER_METHOD";
	}

	echo $output;
?>