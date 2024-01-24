/* Inserts the given team information to the teams table

Example Usage:

DECLARE @Status SMALLINT
EXEC @Status =  [insert_team]
@TeamName_input = 'KSC',
@Region_input = 'East'
SELECT Status = @Status

Blaise Swartwood
*/

Use [Swimming Information Database]
Go

ALTER PROCEDURE [insert_team] (
@TeamName_input varchar(50),
@Region_input varchar(10)
)

AS
SET NOCOUNT ON

-- Making sure the OrderID and ProductID are valid
IF @TeamName_input IS NULL OR @TeamName_input = '' OR @Region_input IS NULL OR @Region_input = ''
BEGIN
	RAISERROR('None of the given values can be null or empty.', 14, 1)
	RETURN(1)
END

IF (SELECT Count(TeamName)
	FROM [Team]
	WHERE (TeamName = @TeamName_input)) > 0
BEGIN
	RAISERROR('The given team already exists in the database.', 14, 2)
	RETURN(2)
END


-- inserting into the table now with the appropriate details
INSERT INTO [Team] 
 ([TeamName], [Region])
VALUES (@TeamName_input, @Region_input)

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