<?php
include 'db_connection.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $full_name = $_POST["full_name"];
    $username = $_POST["username"];
    $email = $_POST["email"];
    $password = password_hash($_POST["password"], PASSWORD_BCRYPT);

    
    $check_user = $conn->prepare("SELECT id FROM users WHERE username = ? OR email = ?");
    $check_user->bind_param("ss", $username, $email);
    $check_user->execute();
    $check_user->store_result();

    if ($check_user->num_rows > 0) {
        echo json_encode(["success" => false, "message" => "Username or Email already exists"]);
        exit();
    }
    $check_user->close();

    
    $stmt = $conn->prepare("INSERT INTO users (full_name, username, email, password) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("ssss", $full_name, $username, $email, $password);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "message" => "Registration successful"]);
    } else {
        echo json_encode(["success" => false, "message" => "Error registering user"]);
    }

    $stmt->close();
}
$conn->close();
?>
