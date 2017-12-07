<?php
    require "database.php";
    
    if ($_SERVER['REQUEST_METHOD'] == "POST") {
        if (isset($_POST['username'])) {
            if (isset($_POST['password'])) {
                $mysqli = ConnectToDatabase();
                $username = $mysqli->real_escape_string(trim($_POST['username']));
                $password = $mysqli->real_escape_string(trim($_POST['password']));
            	$sql = "SELECT * FROM users WHERE username = '$username'";
            	$result = $mysqli->query($sql) or die("Bad query: $sql");
            	if ($result->num_rows == 0) {
            		$output = "ERROR_NO_USER_FOUND";
            	} else {
                	$row = $result->fetch_assoc();
                	$password_hash = sha1($password . $row['salt']);
                	if ($password_hash == $row['password']) {
                	    $output = "SUCCESS_LOGGED_IN";
                	} else {
                	    $output = "ERROR_INCORRECT_PASSWORD";
                	}
            	}
            } else {
                $output = "ERROR_NO_PASSWORD";
            }
        } else {
            $output = "ERROR_NO_USERNAME";
        }
    } else {
        $output = "ERROR_SERVER_METHOD";
    }
    
    echo $output;
?>