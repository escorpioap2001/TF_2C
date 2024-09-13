-- Parking database
CREATE DATABASE parkingdb;
USE parkingdb;
CREATE TABLE parking (
    id VARCHAR(255) PRIMARY KEY,
    direction VARCHAR(255),
    bikes_capacity INT,
    latitude FLOAT,
    longitude FLOAT
);
-- Inserta datos en la tabla parking
INSERT INTO parking (id, direction, bikes_capacity, latitude, longitude) 
VALUES 
  ('P1', 'Avenida de la Paz', 100, 40.4168, -3.7038),
  ('P2', 'Calle Gran Vía', 80, 40.4204, -3.7050),
  ('P3', 'Plaza Mayor', 120, 40.4155, -3.7075),
  ('P4', 'Calle Serrano', 90, 40.4276, -3.6880),
  ('P5', 'Paseo del Prado', 150, 40.4131, -3.6918);


-- Station database
CREATE DATABASE stationdb;
USE stationdb;
CREATE TABLE station (
    id VARCHAR(255) PRIMARY KEY,
    direction VARCHAR(255),
    latitude FLOAT,
    longitude FLOAT
);
-- Inserta datos en la tabla station
INSERT INTO station (id, direction, latitude, longitude) 
VALUES 
  ('S1', 'Calle Alcalá', 40.4203, -3.6949),
  ('S2', 'Paseo de Recoletos', 40.4224, -3.6922),
  ('S3', 'Plaza de España', 40.4232, -3.7126),
  ('S4', 'Parque del Retiro', 40.4139, -3.6838),
  ('S5', 'Calle Mayor', 40.4155, -3.7082);

-- Crea un usuario con permisos para acceder desde cualquier dispositivo
CREATE USER 'user'@'%' IDENTIFIED BY 'twcam23$';
GRANT ALL PRIVILEGES ON *.* TO 'user'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
