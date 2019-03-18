
Lors du lancement du projet, si vous n'avez pas l'archive Mongodb,
je l'ai mise dans le dossier ressource, il faut l'aujouter à la 
structure du projet

// Launch Docker container to get no sql database (Mongo)
docker run -it -p 27017:27017 mongo:latest

// CONNECT A CLIENT
mongo --port 27017
mongo mongodb://localhost:27017

// Create database and collections
use jMessanger
db.createCollection("User")
db.createCollection("Friends")
db.createCollection("Salon")
db.createCollection("Message")


// Add this users test to your mongodb
db.getCollection('User').insertMany([
   {
       "identifiant" : "juando",
       "password" : "random",
       "nom" : "dangleterre",
       "prenom" : "thomas",
       "avatar_number" : 1,
       "_id" : 1
   },
   {
       "identifiant" : "thotho",
       "password" : "random",
       "nom" : "casier",
       "prenom" : "jean",
        "avatar_number" : 2,
        "_id" : 2

   },
   {
       "identifiant" : "angel",
       "password" : "random",
       "nom" : "seitel",
       "prenom" : "benjo",
       "avatar_number" : 3,
       "_id" : 3
   },
   {
       "identifiant" : "richard",
       "password" : "random",
       "nom" : "florent",
        "prenom" : "nicolas",
        "avatar_number" : 4,
        "_id" : 4
   }
   ]);


// Add this messages test to your mongodb
db.getCollection('Friends').insertMany([
   {
       "_id" : 1,
       "friends": [2, 3, 4]
   },
   {
       "_id" : 2,
       "friends": [1, 3, 4]
   },
   {
       "_id" : 3,
       "friends": [1, 2, 4]
   },
   {
       "_id" : 4,
       "friends": [1, 2, 3]
   }
   ]);
   
   
//
db.getCollection('Salon').insertMany([
   {
      "_id" : ObjectId("5c8d3f4f6a63af115ddcf269"),
          "identifiantSalon" : "salonTesii",
          "createur" : "angel",
          "membres" : [ 
              "thotho", 
              "richard", 
              "angel"
          ]
   }
   ]);
  
db.getCollection('Message').insertMany([

{
    "_id" : ObjectId("5c8af5aaa34e4706afae3192"),
    "envoyeur" : "thotho",
    "receveur" : "juando",
    "message" : "coucou",
    "date" : ISODate("2019-03-15T00:45:30.202Z")
},
{
    "_id" : ObjectId("5c8af660a34e4706b3403718"),
    "envoyeur" : "thotho",
    "receveur" : "juando",
    "message" : "salut juando cc",
    "date" : ISODate("2019-03-15T00:48:31.997Z")
},
{
    "_id" : ObjectId("5c8af667a34e4706b3403719"),
    "envoyeur" : "thotho",
    "receveur" : "juando",
    "message" : "salut juando cc",
    "date" : ISODate("2019-03-15T00:48:39.519Z")
},
{
    "_id" : ObjectId("5c8af6e2a34e4706b340371a"),
    "envoyeur" : "thotho",
    "receveur" : "juando",
    "message" : "1h50",
    "date" : ISODate("2019-03-15T00:50:42.682Z")
}
    ]);