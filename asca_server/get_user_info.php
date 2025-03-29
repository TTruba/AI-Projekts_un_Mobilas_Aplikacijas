<?php
include("db_connection.php");

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $token = $_POST["token"];

    $stmt = $conn->prepare("SELECT full_name, username, email FROM users WHERE token = ?");
    $stmt->bind_param("s", $token);
    $stmt->execute();
    $stmt->bind_result($full_name, $username, $email);

    if ($stmt->fetch()) {
        echo json_encode([
            "status" => "success",
            "full_name" => $full_name,
            "username" => $username,
            "email" => $email
        ]);
    } else {
        echo json_encode(["status" => "error", "message" => "Invalid token"]);
    }

    $stmt->close();
    $conn->close();
}
?>
