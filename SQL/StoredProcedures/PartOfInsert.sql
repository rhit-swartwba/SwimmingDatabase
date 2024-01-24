/* Inserts the given swimmer-team information to the part of table

Example Usage:

DECLARE @Status SMALLINT
EXEC @Status =  [insert_partof]
@PersonFName_1 = 'Blaise',
@PersonLName_2 = 'Swartwood',
@TeamName_3 = 'KSC'
@Group_4 = 'Mano',
@HoursPerWeek_5 = 12
SELECT Status = @Status

Blaise Swartwood
*/

Use [Swimming Information Database]
Go

CREATE PROCEDURE [insert_partof] (
@PersonFName_1 varchar(40),
@PersonLName_2 varchar(40),
@TeamName_3 varchar(50),
@Group_4 varchar(50),
@HoursPerWeek_5 int
)

AS
SET NOCOUNT ON

IF @PersonFName_1 IS NULL OR @PersonFName_1 = '' OR @PersonLName_2 IS NULL OR @PersonLName_2 = '' 
OR @TeamName_3 IS NULL OR @TeamName_3 = '' OR @Group_4 IS NULL OR @Group_4 = ''
OR @HoursPerWeek_5 IS NULL OR @HoursPerWeek_5 = ''
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN(1)
END

DECLARE @PID int
DECLARE @TID int
SELECT @PID = ID FROM Person WHERE FName = @PersonFName_1 AND LName = @PersonLName_2
SELECT @TID = TeamID FROM Team WHERE TeamName = @TeamName_3

IF (SELECT Count(PersonID)
	FROM [PartOf]
	WHERE (PersonID = @PID AND TeamID = @TID)) > 0
BEGIN
	RAISERROR('The swimmer is already on the team in the database.', 14, 2)
	RETURN(2)
END

-- inserting into the table now with the appropriate details
INSERT INTO [PartOF] 
( [PersonID], [TeamID], [Group], [HoursPerWeek])
VALUES ( @PID, @TID, @Group_4, @HoursPerWeek_5)

-- Check or any errors
DECLARE @Status SMALLINT
SET @Status = @@ERROR
IF @Status <> 0 
BEGIN	
	RAISERROR('An error occured in completing this operation', 14, @status)
	RETURN @status
END
ELSE
BEGIN	
	RETURN(0)
END