/* Deletes the given team information to the teams table

Example Usage:

DECLARE @Status SMALLINT
EXEC @Status =  [delete_team]
@TeamName_input = 'KSC'
SELECT Status = @Status

Blaise Swartwood
*/

Use [Swimming Information Database]
Go

ALTER PROCEDURE [delete_team] (
@TeamName_input varchar(50)
)

AS
SET NOCOUNT ON

-- Making sure the OrderID and ProductID are valid
IF @TeamName_input IS NULL OR @TeamName_input = ''
BEGIN
	RAISERROR('None of the given fields can be null or empty', 14, 1)
	RETURN(1)
END

IF (SELECT Count(TeamName)
	FROM [Team]
	WHERE (TeamName = @TeamName_input)) = 0
BEGIN
	RAISERROR('The given team does not exist in the database.', 14, 2)
	RETURN(2)
END

-- inserting into the table now with the appropriate details
DELETE [Team] 
WHERE ( [TeamName] = @TeamName_input)

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