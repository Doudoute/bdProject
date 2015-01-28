-- 1) Trigger v�rifiant que la date de d�but d’une location est inf�rieure a la date du jour.

-- drop trigger before_create_location;

CREATE OR REPLACE TRIGGER before_create_location BEFORE INSERT
ON LOUE FOR EACH ROW
DECLARE 
	forbidden_create_location EXCEPTION;
	dateDebLocation date;
	date_current date;
BEGIN
    -- Instructions
    select to_date(sysdate,'yyyy/mm/dd:hh:miam') into date_current  from DUAL;
    IF  date_current >= :new.dateDebut THEN
	RAISE forbidden_create_location;
    END IF; 
EXCEPTION WHEN forbidden_create_location THEN Raise_application_error(-20324,'La date de location doit etre inf�rieure ou egale a la date actuelle.');
END;
/
-- select to_date(sysdate,'yyyy/mm/dd:hh:miam')from DUAL;
--Test de CREATION d'une location avec une date inférieure à la date du jour
--insert into LOUE values (2, 0068, to_date('2014/04/25:10:30AM', 'yyyy/mm/dd:hh:miam'), null, null);
--Observation : L'erreur est levée
--Trace : 
--SQL> insert into LOUE values (2, 0067, to_date('2014/04/25:10:30AM', 'yyyy/mm/dd:hh:miam'), null, null);
--insert into LOUE values (2, 0067, to_date('2014/04/25:10:30AM', 'yyyy/mm/dd:hh:miam'), null, null)
--            *
--ERROR at line 1:
--ORA-20324: La date de location doit etre inf??rieure ou egale a la date
--actuelle.
--ORA-06512: at "MICHAULU.BEFORE_CREATE_LOCATION", line 22
--ORA-04088: error during execution of trigger 'MICHAULU.BEFORE_CREATE_LOCATION'

-- 2) Toute location de plus de douze heures est sanctionnée par une amende (effectué à l’update de la location, lorsque le vélo est rendu)
-- IMPOSSIBLE DE FAIRE CETTE MANIPULATION SOUS FORME DE TRIGGER
-- table mutante et donc impossible de faire un update pour ajouter un numero d'amende

-- 3) Un client ayant deux amendes en cours non régularisées ou une amende de plus d’un mois non régularisée ne peut plus louer de vélos, mais il peut toujours rendre un vélo.(Au moment d’une demande de location)

CREATE OR REPLACE TRIGGER before_insert_loue BEFORE INSERT ON LOUE FOR EACH ROW
DECLARE 
	forbidden_create_loue EXCEPTION;
	nombreAmendeNonRegul int;
	dureeJoursAmende float;
	CURSOR c1 IS SELECT * FROM AMENDE natural join LOUE WHERE numClient = :new.numClient AND etatAmende = 'apayer'; 
	UnTuple c1%ROWTYPE;
BEGIN
	SELECT count(numAmende) into nombreAmendeNonRegul FROM AMENDE natural join LOUE WHERE numClient = :new.numClient AND etatAmende = 'apayer'; 
	IF nombreAmendeNonRegul > 2 THEN
		RAISE forbidden_create_loue;
	END IF;
	IF nombreAmendeNonRegul = 1 THEN
		OPEN c1; 
		FETCH c1 INTO UnTuple; 
		WHILE (c1%FOUND) LOOP 
			dureeJoursAmende := UnTuple.dateAmende - current_date; 
			IF dureeJoursAmende > 30.0 THEN
				RAISE forbidden_create_loue;
			END IF;
			FETCH c1 INTO UnTuple;
		END LOOP; 
		CLOSE c1;
	END IF;   
EXCEPTION WHEN forbidden_create_loue THEN Raise_application_error(-20323,'Location interdite : amende de plus d un mois ou deux amendes non régularisée.');
END;
/
--Test de CREATION d'une location avec un client qui possède deux amendes non régularisées
-- SELECT count(numAmende) FROM AMENDE natural join LOUE WHERE numClient = 65;
-- insert into LOUE values (40, 0065, to_date(current_date, 'yyyy/mm/dd:hh:miam'), null, null);
--Observation : L'erreur est levée
--Trace : 
-- SQL> insert into LOUE values (40, 0065, to_date(current_date, 'yyyy/mm/dd:hh:miam'), null, null);
-- insert into LOUE values (40, 0065, to_date(current_date, 'yyyy/mm/dd:hh:miam'), null, null)
--             *
-- ERROR at line 1:
-- ORA-20324: La date de location doit etre inférieure ou egale a la date
-- actuelle.
-- ORA-06512: at "MICHAULU.BEFORE_CREATE_LOCATION", line 11
-- ORA-04088: error during execution of trigger 'MICHAULU.BEFORE_CREATE_LOCATION'

--Test de CREATION d'une location avec un client qui possède une amende deplus d'un mois non régularisées
-- SELECT count(numAmende) FROM AMENDE natural join LOUE WHERE numClient = 65;
-- insert into LOUE values (41, 0076, to_date(current_date, 'yyyy/mm/dd:hh:miam'), null, null);
--Observation : L'erreur est levée
--Trace : 
-- INSERT INTO LOUE values (2, 0067, to_date('2014/04/25:10:30AM', 'yyyy/mm/dd:hh:miam'), null, null)
-- SELECT (dateAmende - current_date) FROM AMENDE natural join LOUE WHERE numClient = 13 AND etatAmende = "apayer";
SHOW ERROR TRIGGER before_insert_loue;

-- 4) Le vélo qui vient d’être récupéré peut être déclaré en panne avant un délai maximum de trois minutes de location.(Au moment du rendu du vélo)
-- 5) Il est impossible de louer plusieurs vélos dans la même période. (Au moment de la location)
-- 6) Si la réservation d’un client est en conflit avec les réservations d’autres clients alors la réservations ne peut pas aboutir. (Au moment de la création d’une réservation)
-- 7) Pour un abonné qui bénéficie de la remise Vplus, cette remise sera appliquée immédiatement sur son prochain trajet. (Au moment de l’insertion dans location)
-- 8) Les remises Vplus ne sont pas cumulables.
-- 9) Pour les non abonnés disposant d’une remise Vplus, le code identifiant pour activer la remise est valable un mois. (Au moment d’une insertion dans location)
-- 10) Pour les routines, chaque action doit être validée avant de passer à la suivante. (Au moment de l’update d’une tache)
-- 11) La date d’une réservation doit être postérieure à la date courante. (Au moment de l’insertion dans réservation)
-- 12) Empêcher réabonnement si un abonnement est déjà en cours
-- 13) Mettre toutes les routines à état noneffectuée lorsqu'une nouvelle journée débute
-- 14) Le même vélo ne peut pas être loué par une personne s'il est déjà loué
-- 15) Un vélo ne peut pas être loué s'il est déjà réservé