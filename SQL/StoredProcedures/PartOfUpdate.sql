/* Updates the given swimmer-team information from the PartOf table

Example Usage:

DECLARE @Status SMALLINT
EXEC @Status =  [update_partof]
@PersonFName_1 = 'Blaise',
@PersonLName_2 = 'Swartwood',
@TeamName_3 = 'KSC'
@Group_4 = 'Nationals',
@HoursPerWeek_5 = 12
SELECT Status = @Status

Blaise Swartwood
*/


CREATE PROCEDURE [dbo].[update_partof] (
@PID int,
@TeamName_3 varchar(50),
@Group_4 varchar(50),
@HoursPerWeek_5 int,
@OldTeamID int
)

AS
SET NOCOUNT ON

IF @PID IS NULL
OR @TeamName_3 IS NULL OR @TeamName_3 = '' OR @Group_4 IS NULL OR @Group_4 = ''
OR @HoursPerWeek_5 IS NULL OR @HoursPerWeek_5 = ''
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN(1)
END

DECLARE @TID int
SELECT @TID = TeamID FROM Team WHERE TeamName = @TeamName_3

IF (SELECT Count(PersonID)
	FROM [PartOf]
	WHERE (PersonID = @PID AND TeamID = @TID)) = 0
BEGIN
	RAISERROR('The swimmer is not on the team in the database.', 14, 2)
	RETURN(2)
END

-- inserting into the table now with the appropriate details
Update [PartOf] 
SET [TeamID] = @TID, [Group] = @Group_4, HoursPerWeek = @HoursPerWeek_5
WHERE PersonID = @PID AND TeamID = @OldTeamID

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