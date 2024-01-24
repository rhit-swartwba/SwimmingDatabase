/* Inserts the given event information to the events table

Example Usage:

DECLARE @Status SMALLINT
EXEC @Status =  [insert_event]
@Distance_1 = 200,
@Stroke_2 = 'Butterfly',
@Unit_3 = 'SCY'
SELECT Status = @Status

Blaise Swartwood
*/

Use [Swimming Information Database]
Go

CREATE PROCEDURE [insert_event] (
@Distance_1 int,
@Stroke_2 varchar(40),
@Unit_3 varchar(10)
)

AS
SET NOCOUNT ON

-- Making sure the OrderID and ProductID are valid
IF @Distance_1 IS NULL OR @Distance_1 = '' OR @Stroke_2 IS NULL OR @Stroke_2 = ''
   OR @Unit_3 IS NULL OR @Unit_3 = ''
BEGIN
	RAISERROR('None of the given values can be null or empty.', 14, 1)
	RETURN(1)
END

DECLARE @EveID int
SELECT @EveID = EventID FROM Event WHERE Distance = @Distance_1 AND Unit = @Unit_3 AND Stroke = @Stroke_2

IF @EveID != 0
BEGIN
	RAISERROR('The given event already exists in the database.', 14, 2)
	RETURN(2)
END


-- inserting into the table now with the appropriate details
INSERT INTO [Event] 
 ([Distance], [Stroke], [Unit])
VALUES (@Distance_1, @Stroke_2, @Unit_3)

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