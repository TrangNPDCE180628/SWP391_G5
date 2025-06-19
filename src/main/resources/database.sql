-- Create Users table
CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    fullname NVARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Insert sample admin user
INSERT INTO Users (username, password, fullname, gender, role)
VALUES ('admin', 'admin', 'Administrator', 'male', 'admin');

-- Insert sample customer user
INSERT INTO Users (username, password, fullname, gender, role)
VALUES ('customer', 'customer', 'Customer User', 'male', 'customer'); 