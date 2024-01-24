/* Deletes the givens information from the PartOf Table

Example Usage:

DECLARE @Status SMALLINT
EXEC @Status =  [delete_partof]
@PersonFName_1 = 'Blaise',
@PersonLName_2 = 'Swartwood',
@TeamName_3 = 'KSC'
SELECT Status = @Status

Blaise Swartwood
*/

Use [Swimming Information Database]
Go

ALTER PROCEDURE [delete_partof] (
@PersonFName_1 varchar(40),
@PersonLName_2 varchar(40),
@TeamName_3 varchar(50)
)

AS
SET NOCOUNT ON

IF @PersonFName_1 IS NULL OR @PersonFName_1 = '' OR @PersonLName_2 IS NULL OR @PersonLName_2 = ''
OR @TeamName_3 IS NULL OR @TeamName_3 = ''
BEGIN
	RAISERROR('None of the given fields can be null or empty', 14, 1)
	RETURN(1)
END

DECLARE @PID int
DECLARE @TID int
SELECT @PID = ID FROM Person WHERE FName = @PersonFName_1 AND LName = @PersonLName_2
SELECT @TID = TeamID FROM Team WHERE TeamName = @TeamName_3

IF (SELECT Count(PersonID)
	FROM [PartOf]
	WHERE (PersonID = @PID AND TeamID = @TID)) = 0
BEGIN
	RAISERROR('The swimmer is not currently on the team in the database.', 14, 2)
	RETURN(2)
END

-- deleting into the table now with the appropriate details
DELETE [PartOf] 
WHERE (PersonID = @PID AND TeamID = @TID)

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