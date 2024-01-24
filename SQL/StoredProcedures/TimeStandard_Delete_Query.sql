Use [Swimming Information Database]
GO

Create Procedure delete_TimeStandards (
@Distance int,
@Unit varchar(3),
@Stroke varchar(15),
@Level varchar(20)
)

AS
SET NOCOUNT ON

IF @Distance IS NULL OR @Distance = '' OR
@Unit IS NULL OR @Unit = '' OR
@Stroke IS NULL OR @Stroke = '' OR
@Level IS NULL OR @Level = ''
BEGIN
	RAISERROR('None of the given fields can be null', 14, 1)
	RETURN 1
END

DECLARE @Event_ID int
SET @Event_ID = (select EventID from [Event] Where Distance = @Distance AND
					Stroke = @Stroke AND Unit = @Unit)

IF  (SELECT Count(@Event_ID)
	FROM [TimeStandard]
	WHERE (EventID = @Event_ID AND [Level] = @Level)) = 0
BEGIN
	RAISERROR('The TimeStandard given does not exist in the database.', 14, 2)
	RETURN 2
END

-- deleting into the table now with the appropriate details
DELETE [TimeStandard] 
WHERE ( EventID = @Event_ID AND [Level] = @Level)

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