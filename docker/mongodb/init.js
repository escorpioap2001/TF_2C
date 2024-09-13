// Inserta datos en la colección parking en la base de datos parkingdb
db.getSiblingDB("parkingdb").parking.insertMany([
    {
      "id_parking": "P1",
      "operation": "rent",
      "bikesAvailable": 50,
      "freeParkingSpots": 20,
      "timestamp": ISODate('2024-05-25T11:30:25.310Z')
    },
    {
      "id_parking": "P2",
      "operation": "rent",
      "bikesAvailable": 45,
      "freeParkingSpots": 25,
      "timestamp": ISODate('2024-05-25T11:30:25.310Z')
    },
    {
      "id_parking": "P3",
      "operation": "return",
      "bikesAvailable": 60,
      "freeParkingSpots": 10,
      "timestamp": ISODate('2024-05-25T11:30:25.310Z')
    },
    {
      "id_parking": "P4",
      "operation": "rent",
      "bikesAvailable": 70,
      "freeParkingSpots": 5,
      "timestamp": ISODate('2024-05-25T11:30:25.310Z')
    },
    {
      "id_parking": "P5",
      "operation": "return",
      "bikesAvailable": 90,
      "freeParkingSpots": 15,
      "timestamp": ISODate('2024-05-25T11:30:25.310Z')
    }
  ]);
  
  // Inserta datos en la colección station en la base de datos stationdb
  db.getSiblingDB("stationdb").station.insertMany([
    {
      "id_station": "S1",
      "timestamp": ISODate('2024-05-25T11:30:25.310Z'),
      "nitricOxides": 10.5,
      "nitrogenDioxides": 5.2,
      "VOCs_NMHC": 6.8,
      "PM2_5": 7.1
    },
    {
      "id_station": "S2",
      "timestamp": ISODate('2024-05-25T11:30:25.310Z'),
      "nitricOxides": 11.2,
      "nitrogenDioxides": 6.5,
      "VOCs_NMHC": 7.3,
      "PM2_5": 8.0
    },
    {
      "id_station": "S3",
      "timestamp": ISODate('2024-05-25T11:30:25.310Z'),
      "nitricOxides": 9.8,
      "nitrogenDioxides": 4.7,
      "VOCs_NMHC": 5.9,
      "PM2_5": 6.5
    },
    {
      "id_station": "S4",
      "timestamp": ISODate('2024-05-25T11:30:25.310Z'),
      "nitricOxides": 12.3,
      "nitrogenDioxides": 7.0,
      "VOCs_NMHC": 8.2,
      "PM2_5": 9.1
    },
    {
      "id_station": "S5",
      "timestamp": ISODate('2024-05-25T11:30:25.310Z'),
      "nitricOxides": 9.5,
      "nitrogenDioxides": 4.2,
      "VOCs_NMHC": 6.0,
      "PM2_5": 6.8
    }
  ]);
  

  db.getSiblingDB("ayuntamientodb").ayuntamiento.insertOne({
    "timeStamp": ISODate('2024-05-25T11:30:25.310Z'),
    "aggregatedData": [
       {
          "id_parking": 1,
          "average_bikes_available": 45.5,
          "airQuality": {
             "nitricOxides": 10.5,
             "nitrogenDioxides": 5.2,
             "VOCs_NMHC": 6.8,
             "PM2_5": 7.1
          }
       },
       {
          "id_parking": 2,
          "average_bikes_available": 55.6,
          "airQuality": {
             "nitricOxides": 11.2,
             "nitrogenDioxides": 6.5,
             "VOCs_NMHC": 7.3,
             "PM2_5": 8.0
          }
       }
    ]
 });
