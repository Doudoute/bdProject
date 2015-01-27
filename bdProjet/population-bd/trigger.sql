-- 1) Trigger vérifiant que la date de début dâ€™une location est inférieure a la date du jour.

-- drop trigger before_create_location;

CREATE OR REPLACE TRIGGER before_create_location BEFORE INSERT
ON LOUE FOR EACH ROW
DECLARE 
	forbidden_create_location EXCEPTION;
	dateDebLocation date;
	date_current date;
BEGIN
    -- Instructions
    date_current := current_date;
    IF  date_current >= :new.dateDebut THEN
	RAISE forbidden_create_location;
    END IF; 
EXCEPTION WHEN forbidden_create_location THEN Raise_application_error(-20324,'La date de location doit etre inférieure ou egale a la date actuelle.');
END;
/

--Test de CREATION d'une location avec une date inférieure Ã  la date du jour
--insert into LOUE values (2, 0067, to_date('2014/04/25:10:30AM', 'yyyy/mm/dd:hh:miam'), null, null);
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