<?php
$host = "localhost";
$dbname = "antiscamdb";
$user = "root";
$pass = "root";

$conn = new mysqli($host, $user, $pass, $dbname);

if ($conn->connect_error) {
    die(json_encode(["success" => false, "message" => "Database connection failed"]));
}
?>
