-- 1) Trigger verifiant que la date de debut d une location est inferieure a la date du jour.

-- drop trigger before_create_location;

CREATE OR REPLACE TRIGGER before_create_location BEFORE INSERT
ON LOUE FOR EACH ROW
DECLARE 
	forbidden_create_location EXCEPTION;
	dateDebLocation date;
	date_current date;
	date_temp varchar(20);
BEGIN
    -- Instructions
    select to_char(SYSDATE,'yyyy/mm/dd:hh:miam') into date_temp from DUAL;
    select to_date(date_temp,'yyyy/mm/dd:hh:miam') into date_current from DUAL;
    IF  date_current > :new.dateDebut THEN
	RAISE forbidden_create_location;
    END IF; 
EXCEPTION WHEN forbidden_create_location THEN Raise_application_error(-20324,'La date de location doit etre supérieure ou egale a la date actuelle.');
END;
/
-- select to_date(sysdate,'yyyy/mm/dd:hh:miam')from DUAL;
--Test de CREATION d'une location avec une date inferieure Ã  la date du jour
--insert into LOUE values (2, 0069, to_date('2015/01/28:10:30PM', 'yyyy/mm/dd:hh:miam'), null, null);
--Observation : L'erreur est levee
--Trace : 
--SQL> insert into LOUE values (2, 0067, to_date('2014/04/25:10:30AM', 'yyyy/mm/dd:hh:miam'), null, null);
--insert into LOUE values (2, 0067, to_date('2014/04/25:10:30AM', 'yyyy/mm/dd:hh:miam'), null, null)
--            *
--ERROR at line 1:
--ORA-20324: La date de location doit etre inferieure ou egale a la date
--actuelle.
--ORA-06512: at "MICHAULU.BEFORE_CREATE_LOCATION", line 22
--ORA-04088: error during execution of trigger 'MICHAULU.BEFORE_CREATE_LOCATION'

-- 2) Toute location de plus de douze heures est sanctionnee par une amende (effectue a  l update de la location, lorsque le velo est rendu)
-- IMPOSSIBLE DE FAIRE CETTE MANIPULATION SOUS FORME DE TRIGGER
-- table mutante et donc impossible de faire un update pour ajouter un numero d'amende

-- 3) Un client ayant deux amendes en cours non regularisees ou une amende de plus d un mois non regularisee ne peut plus louer de velos, mais il peut toujours rendre un velo.(Au moment d une demande de location)

CREATE OR REPLACE TRIGGER before_insert_loue BEFORE INSERT ON LOUE FOR EACH ROW
DECLARE 
	forbidden_create_loue EXCEPTION;
	nombreAmendeNonRegul int;
	dureeJoursAmende float;
	CURSOR c1 IS SELECT * FROM AMENDE natural join LOUE WHERE numClient = :new.numClient AND etatAmende = 'apayer'; 
	UnTuple c1%ROWTYPE;
BEGIN
	SELECT count(*) into nombreAmendeNonRegul FROM AMENDE natural join LOUE WHERE numClient = :new.numClient AND etatAmende = 'apayer'; 
	IF nombreAmendeNonRegul >= 2 THEN
		RAISE forbidden_create_loue;
	ELSIF nombreAmendeNonRegul > 0 THEN
		OPEN c1; 
		FETCH c1 INTO UnTuple; 
		WHILE (c1%FOUND) LOOP 
			dureeJoursAmende := current_date - UnTuple.dateAmende; 
			IF dureeJoursAmende > 30.0 THEN
				RAISE forbidden_create_loue;
			END IF;
			FETCH c1 INTO UnTuple;
		END LOOP; 
		CLOSE c1;
	END IF;   
EXCEPTION WHEN forbidden_create_loue THEN Raise_application_error(-20323,'Location interdite : amende de plus d un mois ou deux amendes non rÃ©gularisÃ©e.');
END;
/
--Test de CREATION d'une location avec un client qui possede deux amendes non regularisees
-- SELECT count(numAmende) FROM AMENDE natural join LOUE WHERE numClient = 65;
-- insert into LOUE values (40, 0065, to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam'), 'yyyy/mm/dd:hh:miam'), null, null);
--Observation : L'erreur est levee
--Trace : 
-- SQL> insert into LOUE values (40, 0065, to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam'), 'yyyy/mm/dd:hh:miam'), null, null);
-- insert into LOUE values (40, 0065, to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam'), 'yyyy/mm/dd:hh:miam'), null, null)
--             *
-- ERROR at line 1:
-- ORA-20323: Location interdite : amende de plus d un mois ou deux amendes non
-- regularisee.
-- ORA-06512: at "MICHAULU.BEFORE_INSERT_LOUE", line 24
-- ORA-04088: error during execution of trigger 'MICHAULU.BEFORE_INSERT_LOUE'

--Test de CREATION d'une location avec un client qui possede une amende deplus d un mois non regularisees
-- SELECT count(numAmende) FROM AMENDE natural join LOUE WHERE numClient = 65;
-- insert into LOUE values (42, 0076, to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam'),'yyyy/mm/dd:hh:miam'), null, null);
--Observation : L'erreur est levee
--Trace : 
-- SQL> insert into LOUE values (42, 0076, to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam'),'yyyy/mm/dd:hh:miam'), null, null);
-- insert into LOUE values (42, 0076, to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam'),'yyyy/mm/dd:hh:miam'), null, null)
--             *
-- ERROR at line 1:
-- ORA-20323: Location interdite : amende de plus d un mois ou deux amendes non
-- regularisee.
-- ORA-06512: at "MICHAULU.BEFORE_INSERT_LOUE", line 23
-- ORA-04088: error during execution of trigger 'MICHAULU.BEFORE_INSERT_LOUE'

-- 4) Le velo qui vient d etre recupere peut etre declare en panne avant un delai maximum de trois minutes de location.
-- Trop complique à gerer au niveau des triggers, la contrainte sera geree au niveau applicatif.

-- 5) Il est impossible de louer plusieurs velos dans la meme periode. 
CREATE OR REPLACE TRIGGER before_insert_loue2 BEFORE INSERT ON LOUE FOR EACH ROW
DECLARE 
	forbidden_create_loue EXCEPTION;
	nombreLocation int;
BEGIN
	SELECT count(*) into nombreLocation FROM LOUE WHERE numClient = :new.numClient AND dateFin IS null; 
	IF nombreLocation > 0 THEN
		RAISE forbidden_create_loue;
	END IF;   
EXCEPTION WHEN forbidden_create_loue THEN Raise_application_error(-20322,'Location interdite : une location est deja en cours.');
END;
/
-- Test de CREATION d'une location avec un client qui possede deja une location en cours (velo non rendu)
-- insert into LOUE values (42, 0020, to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'), null, null);
-- INSERT INTO Loue VALUES (36,0020,to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'),NULL,NULL);
-- Observation : L'erreur est levee
-- Trace : 
-- SQL> insert into LOUE values (42, 0020, to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'), null, null);
-- insert into LOUE values (42, 0020, to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'), null, null)
--             *
-- ERROR at line 1:
-- ORA-20322: Location interdite : une location est d?j? en cours.
-- ORA-06512: at "MICHAULU.BEFORE_INSERT_LOUE2", line 9
-- ORA-04088: error during execution of trigger 'MICHAULU.BEFORE_INSERT_LOUE2'

-- SHOW ERROR TRIGGER before_insert_loue2;

-- 6) Si la reservation d un client est en conflit avec les reservations d autres clients alors la reservations ne peut pas aboutir. (Au moment de la creation d une reservation)



-- 7) Pour un abonne qui beneficie de la remise Vplus, cette remise sera appliquee immediatement sur son prochain trajet. 
-- A faire au niveau de l application

-- 8) Les remises Vplus ne sont pas cumulables --> Suppression de la remise la plus ancienne avant l ajout de la nouvelle remise

CREATE OR REPLACE TRIGGER before_insert_remise BEFORE INSERT ON RemiseNonAbonne FOR EACH ROW
DECLARE 
	nombreRemise int;
	dateLaPlusVieille date;
BEGIN
	SELECT count(*) into nombreRemise FROM RemiseNonAbonne WHERE numNonAbonne = :new.numNonAbonne; 
	SELECT max(dateRemise) into dateLaPlusVieille FROM RemiseNonAbonne WHERE numNonAbonne = :new.numNonAbonne; 
	IF nombreRemise = 1 THEN
		DELETE FROM RemiseNonAbonne WHERE numNonAbonne = :new.numNonAbonne and dateRemise = dateLaPlusVieille;
	END IF;   
END;
/
-- Test de CREATION d une remise non abonnee a un client qui en possede deja une (la plus recente doit etre gardee)
-- INSERT INTO RemiseNonAbonne VALUES (60,TO_DATE('26/01/2015 19:25','dd/mm/yyyy HH24:MI'),3584);
-- Observation : La remise la plus ancienne est bien supprimee
-- Trace :
-- SQL> select * from remisenonabonne where numnonabonne=60;
-- 
-- NUMNONABONNE DATEREMIS CODEREMISE
-- ------------ --------- ----------
--           60 25-JAN-15       3564
-- SQL> INSERT INTO RemiseNonAbonne VALUES (60,TO_DATE('26/01/2015 19:25','dd/mm/yyyy HH24:MI'),3584);
-- 
-- 1 row created.
-- SQL> select * from remisenonabonne where numnonabonne=60;
-- 
-- NUMNONABONNE DATEREMIS CODEREMISE
-- ------------ --------- ----------
--           60 26-JAN-15       3584

-- 9) Pour les non abonnes disposant d une remise Vplus, le code identifiant pour activer la remise est valable un mois. (Au moment d une insertion dans location)

-- 10) Pour les routines, chaque action doit Ãªtre validee avant de passer a  la suivante. (Au moment de l update d une tache)

-- 11) La date d une reservation doit etre posterieure a  la date courante. (Au moment de l insertion dans reservation)

-- 12) Empecher reabonnement si un abonnement est deja  en cours
-- Impossible depuis modification du modele de donnee, en effet pas d archivage. Seul l abonnement en cours est visible.

-- 13) Mettre toutes les routines a etat non effectuee lorsqu une nouvelle journee debute
-- A gerer au niveau applicatif : impossible de lancer des cron pour lancer un trigger

-- 14) Le meme velo ne peut pas etre loue par une personne s il est deja  loue

CREATE OR REPLACE TRIGGER before_insert_loue3 BEFORE INSERT ON LOUE FOR EACH ROW
DECLARE 
	forbidden_create_loue EXCEPTION;
	nombreLocationVelo int;
BEGIN
	SELECT count(*) into nombreLocationVelo FROM LOUE WHERE numVelo = :new.numVelo AND dateFin IS null; 
	IF nombreLocationVelo > 0 THEN
		RAISE forbidden_create_loue;
	END IF;   
EXCEPTION WHEN forbidden_create_loue THEN Raise_application_error(-20322,'Location interdite : le velo est deja loue.');
END;
/
-- Test de CREATION d'une location avec un velo qui est deja loue
-- insert into LOUE values (43, 0022, to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'), null, null);
-- INSERT INTO Loue VALUES (43,0021,to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'),NULL,NULL);
-- Observation : L'erreur est levee
-- Trace : 
-- SQL> insert into LOUE values (43, 0022, to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'), null, null);
-- 
-- 1 row created.
-- 
-- SQL> INSERT INTO Loue VALUES (43,0021,to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'),NULL,NULL);
-- INSERT INTO Loue VALUES (43,0021,to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'),NULL,NULL)
--             *
-- ERROR at line 1:
-- ORA-20322: Location interdite : le velo est deja loue.
-- ORA-06512: at "MICHAULU.BEFORE_INSERT_LOUE3", line 9
-- ORA-04088: error during execution of trigger 'MICHAULU.BEFORE_INSERT_LOUE3'

-- 15) Un velo ne peut pas etre loue s il est deja reserve

CREATE OR REPLACE TRIGGER before_insert_loue4 BEFORE INSERT ON LOUE FOR EACH ROW
DECLARE 
	forbidden_insert_loue EXCEPTION;
	veloReserve int;
BEGIN
	SELECT count(*) into veloReserve FROM reserve WHERE numVelo = :new.numVelo; 
	IF veloReserve > 0 THEN
		RAISE forbidden_insert_loue;
	END IF;   
EXCEPTION WHEN forbidden_insert_loue THEN Raise_application_error(-20321,'Location interdite : le velo est deja reserve.');
END;
/
-- Test de CREATION d'une location avec un velo qui est deja reeserve pour la journee
-- INSERT INTO loue VALUES (014,0015,to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'),NULL,NULL); --> fonctionne
-- INSERT INTO loue VALUES (01,0015,to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'),NULL,NULL);
-- Observation : L'erreur est levee
-- Trace :
-- SQL> INSERT INTO loue VALUES (01,0015,to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'),NULL,NULL);
-- INSERT INTO loue VALUES (01,0015,to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'),NULL,NULL)
--             *
-- ERROR at line 1:
-- ORA-20321: Location interdite : le velo est deja reserve.
-- ORA-06512: at "MICHAULU.BEFORE_INSERT_LOUE4", line 9
-- ORA-04088: error during execution of trigger 'MICHAULU.BEFORE_INSERT_LOUE4'

-- 16) Un client non abonne ne doit pas pouvoir reserver de velo
CREATE OR REPLACE TRIGGER before_insert_reservation BEFORE INSERT ON RESERVE FOR EACH ROW
DECLARE 
	forbidden_insert_reserve EXCEPTION;
	abonne int;
BEGIN
	SELECT count(*) into abonne FROM ABONNE WHERE numAbonne = :new.numClient; 
	IF abonne = 0 THEN
		RAISE forbidden_insert_reserve;
	END IF;   
EXCEPTION WHEN forbidden_insert_reserve THEN Raise_application_error(-20321,'Reservation interdite : le client n est pas abonne.');
END;
/
-- Test de CREATION d'une reservation avec un client qui n est pas abonne
-- insert into RESERVE values (61, 43, 06); --> Marche bien parce que le client est abonne
-- insert into RESERVE values (43, 22, 06);
-- INSERT INTO Loue VALUES (43,0021,to_date(to_char(SYSDATE,'yyyy/mm/dd:hh:miam') ,'yyyy/mm/dd:hh:miam'),NULL,NULL);
-- Observation : L'erreur est levee
-- Trace : 
-- SQL> insert into RESERVE values (43, 0022, 06);
-- insert into RESERVE values (43, 0022, 06)
--             *
-- ERROR at line 1:
-- ORA-20321: Reservation interdite : le client n est pas abonne.
-- ORA-06512: at "MICHAULU.BEFORE_INSERT_RESERVATION", line 9
-- ORA-04088: error during execution of trigger
-- 'MICHAULU.BEFORE_INSERT_RESERVATION'

-- SHOW ERROR TRIGGER before_insert_loue3;

-- 17) Si le client loue un velo a une station Vplus et qu il le rend a une station Vmoins --> creation d une remise Vplus

-- 18) La periode de debut doit être inferieure à la date de fin de periode

CREATE OR REPLACE TRIGGER before_create_periode BEFORE INSERT
ON PERIODE FOR EACH ROW
DECLARE 
	forbidden_create_periode EXCEPTION;
BEGIN
    -- Instructions
    IF  :new.numSemaineDebut >= :new.numSemaineFin THEN
	RAISE forbidden_create_periode;
    END IF; 
EXCEPTION WHEN forbidden_create_periode THEN Raise_application_error(-20324,'La semaine de debut de periode doit etre inferieure ou egale a la semaine de fin de periode.');
END;
/

-- Test de CREATION d'une periode avec une semaine de debut superieure a la semaine de fin.
-- insert into periode values (14,1,5,'Lundi'); --> Marche bien parce que la chronologie des semaines est respectée
-- insert into periode values (13,5,1,'Lundi');
-- Observation : L'erreur est levee
-- Trace : 
-- SQL> insert into periode values (13,5,1,'Lundi');
-- insert into periode values (13,5,1,'Lundi')
--             *
-- ERROR at line 1:
-- ORA-20324: La semaine de debut de periode doit etre inferieure ou egale a la
-- semaine de fin de periode.
-- ORA-06512: at "MICHAULU.BEFORE_CREATE_PERIODE", line 8
-- ORA-04088: error during execution of trigger 'MICHAULU.BEFORE_CREATE_PERIODE'