<?php

class Activity {
	private $id;
	private $name;
	private $description;
	private $latitude;
	private $longitude;
	private $distance;
	private $location;
	private $tags;
	private $rating;
	private $reviewCount;
	private $price;

	public function getId() {
		return $this->id;
	}

	public function setId($id) {
		$this->id = $id;
	}

	public function getName() {
		return $this->name;
	}

	public function setName($name) {
		$this->name = $name;
	}

	public function getDescription() {
		return $this->description;
	}

	public function setDescription($description) {
		$this->description = $description;
	}

	public function getLatitude() {
		return $this->latitude;
	}

	public function setLatitude($latitude) {
		$this->latitude = $latitude;
	}

	public function getLongitude() {
		return $this->longitude;
	}

	public function setLongitude($longitude) {
		$this->longitude = $longitude;
	}

	public function getDistance() {
		return $this->distance;
	}

	public function setDistance($distance) {
		$this->distance = $distance;
	}

	public function getLocation() {
		return $this->location;
	}

	public function setLocation($location) {
		$this->location = $location;
	}

	public function getTags() {
		return $this->tags;
	}

	public function setTags($tags) {
		$this->tags = $tags;
	}
	
	public function getRating() {
		return $this->rating;
	}

	public function setRating($rating) {
		$this->rating = $rating;
	}
	
	public function getReviewCount() {
		return $this->reviewCount;
	}

	public function setReviewCount($count) {
		$this->reviewCount = $count;
	}

	public function getPrice() {
		return $this->price;
	}

	public function setPrice($price) {
		$this->price = $price;
	}
}

function ConnectToDatabase() {
	// Used to connect to the database
	$db_username = 'centen14_jd';
	$db_password = '7D5xV8qR90!';
	$db_database = 'centen14_funsheet';

	// Your database name is the same as your username
	$mysqli = new mysqli("localhost", $db_username, $db_password, $db_database);

	// Output error info if there was a connection problem
	if ($mysqli->connect_errno)
		die("Failed to connect to MySQL: ($mysqli->connect_errno) $mysqli->connect_error");

	return $mysqli;
}

/**
 * Calculates the great-circle distance between two points, with
 * the Vincenty formula.
 * @param float $latitudeFrom Latitude of start point in [deg decimal]
 * @param float $longitudeFrom Longitude of start point in [deg decimal]
 * @param float $latitudeTo Latitude of target point in [deg decimal]
 * @param float $longitudeTo Longitude of target point in [deg decimal]
 * @param float $earthRadius Mean earth radius in [m]
 * @return float Distance between points in [m] (same as earthRadius)
 */
function calculateDistance($latitudeFrom, $longitudeFrom, $latitudeTo, $longitudeTo, $earthRadius = 6371000) {
    // convert from degrees to radians
    $latFrom = deg2rad($latitudeFrom);
    $lonFrom = deg2rad($longitudeFrom);
    $latTo = deg2rad($latitudeTo);
    $lonTo = deg2rad($longitudeTo);

    $lonDelta = $lonTo - $lonFrom;
    $a = pow(cos($latTo) * sin($lonDelta), 2) + pow(cos($latFrom) * sin($latTo) - sin($latFrom) * cos($latTo) * cos($lonDelta), 2);
    $b = sin($latFrom) * sin($latTo) + cos($latFrom) * cos($latTo) * cos($lonDelta);
    $angle = atan2(sqrt($a), $b);

    // return $angle * $earthRadius;
    
    return ($angle * $earthRadius) / 1609.344;
}

?>