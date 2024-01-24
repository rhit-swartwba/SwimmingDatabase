/* Updates the given team information to the teams table

Example Usage:

DECLARE @Status SMALLINT
EXEC @Status =  [update_team]
@TeamName_input = 'KSC',
@Region_input = 'West',
SELECT Status = @Status

Blaise Swartwood
*/

Use [Swimming Information Database]
Go

ALTER PROCEDURE [update_team] (
@TeamName_input varchar(50),
@Region_input varchar(10)
)

AS
SET NOCOUNT ON

-- Making sure the OrderID and ProductID are valid
IF @TeamName_input IS NULL OR @TeamName_input = '' OR @Region_input IS NULL OR @Region_input = ''
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN(1)
END

IF (SELECT Count(TeamName)
	FROM [Team]
	WHERE (TeamName = @TeamName_input)) = 0
BEGIN
	RAISERROR('The given team information does not exist in the database.', 14, 2)
	RETURN(2)
END

DECLARE @teamIDVal int
SET @teamIDVal = (select TeamID from Team where TeamName = @TeamName_input)

-- inserting into the table now with the appropriate details
Update [Team] 
Set TeamName = @TeamName_input, Region = @Region_input
Where TeamID = @teamIDVal

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