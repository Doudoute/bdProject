DROP TABLE Periode;
DROP TABLE Reserve;
DROP TABLE Loue;
DROP TABLE Embarque;
DROP TABLE Stocke;
DROP TABLE Ordre;
DROP TABLE Abonne;
DROP TABLE NonAbonne;
DROP TABLE Client;
DROP TABLE Amende;
DROP TABLE Velo;
DROP TABLE VehiculeRegulation;
DROP TABLE Routine;
DROP TABLE Tache;
DROP TABLE Bornette;
DROP TABLE Station;
DROP TABLE Tarif;

CREATE TABLE Station (
adresse VARCHAR(150) PRIMARY KEY NOT NULL,
categorie VARCHAR2(10),
CONSTRAINT ck_categorie CHECK (categorie IN ('Vplus','Vmoins','Vnul'))
);

CREATE TABLE Bornette (
numBornette INTEGER primary key NOT NULL CHECK(numBornette > 0),
etat varchar2(5),
adresse varchar(150) NOT NULL,
CONSTRAINT fk_bornette FOREIGN KEY (adresse) REFERENCES Station(adresse),
CONSTRAINT ck_etat CHECK (etat IN ('OK','HS'))
);

CREATE TABLE Routine (
numRoutine INTEGER PRIMARY KEY NOT NULL CHECK(numRoutine >0),
dateDebut date NOT NULL,
dateFin date NOT NULL
);

CREATE TABLE Tache (
numTache INTEGER NOT NULL PRIMARY KEY CHECK(numTache >0),
description varchar(150) NOT NULL
);

CREATE TABLE VehiculeRegulation (
numVehicule INTEGER NOT NULL PRIMARY KEY CHECK(numVehicule >0),
modele varchar(150) NOT NULL,
numRoutine INTEGER NOT NULL CHECK(numRoutine >0),
dateMiseEnService date NOT NULL,
CONSTRAINT fk_VehiculeRegulation FOREIGN KEY (numRoutine) REFERENCES Routine(numRoutine)
);

CREATE TABLE Velo (
numPuceRFID INTEGER NOT NULL PRIMARY KEY CHECK(numPuceRFID>0),
modele VARCHAR(10),
dateMES date NOT NULL,
etatVelo VARCHAR(25),
CONSTRAINT ck_modele_velo CHECK (modele IN ('pliant', 'electrique', 'VTT', 'route')),
CONSTRAINT ck_etat_velo CHECK (etatVelo IN ('enStation', 'loue', 'embarque', 'réparation', 'enPanne'))
);

CREATE TABLE Amende (
numAmende INTEGER NOT NULL PRIMARY KEY CHECK(numAmende >0),
dateAmende date NOT NULL,
etatAmende varchar(25),
CONSTRAINT ck_etat_amende CHECK (etatAmende IN ('regularisee', 'apayer'))
);

CREATE TABLE Client(
numClient INTEGER NOT NULL PRIMARY KEY CHECK(numClient>0),
numCB VARCHAR(16) NOT NULL,
codeSecret INTEGER NOT NULL CHECK(codeSecret>0)
);

CREATE TABLE NonAbonne (
numNonAbo INTEGER PRIMARY KEY,
CONSTRAINT fk_nonAbonne FOREIGN KEY (numNonAbo) REFERENCES Client(numClient)
);

CREATE TABLE Abonne(
numAbonne INTEGER PRIMARY KEY,
nom VARCHAR(50) NOT NULL,
prenom VARCHAR(50) NOT NULL,
dateNaissance date NOT NULL,
sexe VARCHAR(8),
adresse VARCHAR(150) NOT NULL,
dateAbo date NOT NULL,
etatAbo VARCHAR(7),
CONSTRAINT fk_abonne FOREIGN KEY (numAbonne) REFERENCES Client(numClient),
CONSTRAINT ck_sexe CHECK (sexe IN ('feminin', 'masculin')),
CONSTRAINT ck_etat_abo CHECK (etatAbo IN ('enCours', 'echu'))
);

CREATE TABLE Ordre (
rang INTEGER NOT NULL CHECK (rang > 0),
numRoutine  INTEGER NOT NULL CHECK (numRoutine > 0),  
numTache INTEGER NOT NULL CHECK (numTache > 0),  
etatOrdre VARCHAR(11),
CONSTRAINT pk_ordre PRIMARY KEY (rang, numRoutine, numTache),
CONSTRAINT fk_ordre_numRoutine FOREIGN KEY (numRoutine) REFERENCES Routine(numRoutine),
CONSTRAINT fk_ordre_numTache FOREIGN KEY (numTache) REFERENCES Tache(numTache),
CONSTRAINT ck_etat_ordre CHECK (etatOrdre IN ('nonEffectue', 'valide', 'annule', 'alerte'))
);

CREATE TABLE Stocke (
numVelo INTEGER NOT NULL CHECK(numVelo > 0),
numBornette INTEGER NOT NULL CHECK(numBornette > 0),
CONSTRAINT pk_stocke PRIMARY KEY (numVelo, numBornette),
CONSTRAINT fk_stocke_numVelo FOREIGN KEY (numVelo) REFERENCES Velo(numPuceRFID),
CONSTRAINT fk_stocke_numBornette FOREIGN KEY (numBornette) REFERENCES Bornette(numBornette)
);

CREATE TABLE Embarque(
numVelo integer NOT NULL CHECK(numVelo > 0),
dateEbq date NOT NULL,
numVehicule integer NOT NULL CHECK(numVehicule > 0),
adresseDep varchar(150) NOT NULL,
adresseArr varchar(150),
CONSTRAINT pk_embarque PRIMARY KEY (numVelo, dateEbq),
CONSTRAINT fk_embarque_numVelo FOREIGN KEY (numVelo) REFERENCES Velo(numPuceRFID),
CONSTRAINT fk_embarque_numVehicule FOREIGN KEY (numVehicule) REFERENCES VehiculeRegulation(numVehicule)
);

CREATE TABLE Loue (
numVelo integer NOT NULL CHECK(numVelo > 0),
numClient integer NOT NULL CHECK(numClient > 0),
dateDebut date NOT NULL,
numAmende integer CHECK(numAmende > 0),
dateFin date,
CONSTRAINT pk_loue PRIMARY KEY(numVelo, numClient, dateDebut),
CONSTRAINT fk_loue_numVelo FOREIGN KEY (numVelo) REFERENCES Velo(numPuceRFID),
CONSTRAINT fk_loue_numClient FOREIGN KEY (numClient) REFERENCES Client(numClient),
CONSTRAINT fk_loue_numAmende FOREIGN KEY (numAmende) REFERENCES Amende(numAmende)
);

CREATE TABLE Reserve (
numClient integer NOT NULL CHECK(numClient > 0),
numVelo integer NOT NULL CHECK(numVelo > 0),
numPeriode integer NOT NULL,
CONSTRAINT pk_reserve PRIMARY KEY(numClient,numVelo,numPeriode),
CONSTRAINT fk_reserve_numClient FOREIGN KEY (numClient) REFERENCES Client(numClient),
CONSTRAINT fk_reserve_numVelo FOREIGN KEY (numVelo) REFERENCES Velo(numPuceRFID)
);

CREATE TABLE Periode (
numPeriode integer NOT NULL CHECK (numPeriode > 0 and numPeriode<53),
numSemaineDebut integer CHECK (numSemaineDebut > 0  and numSemaineDebut<53),
numSemaineFin integer CHECK (numSemaineFin > 0  and numSemaineFin<53),
jour varchar(8),
CONSTRAINT pk_periode PRIMARY KEY (numPeriode),
CONSTRAINT ck_jour_semaine CHECK (jour IN ('Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi', 'Dimanche'))
);

CREATE TABLE Tarif (
annee date NOT NULL PRIMARY KEY,
montantLocation integer NOT NULL CHECK(montantLocation > 0)
);

CREATE TABLE RemiseNonAbonne (
numNonAbonne integer NOT NULL PRIMARY KEY,
dateRemise date NOT NULL,
codeRemise UNIQUE integer NOT NULL,
CONSTRAINT fk_remise_non_abonne FOREIGN KEY (numNonAbonne) REFERENCES NonAbonne(numNonAbo)
);

CREATE TABLE RemiseAbonne (
numAbonne integer NOT NULL PRIMARY KEY,
CONSTRAINT fk_remise_abonne FOREIGN KEY (numAbonne) REFERENCES Abonne(numAbonne)
);