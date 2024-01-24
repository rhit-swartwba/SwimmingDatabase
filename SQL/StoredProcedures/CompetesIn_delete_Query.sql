Use [Swimming Information Database]
GO

Create Procedure delete_CompetesIn (
@Distance int,
@Unit varchar(3),
@Stroke varchar(15),
@Fname varchar(20),
@Lname varchar(20)
)

AS
SET NOCOUNT ON

IF @Distance IS NULL OR @Distance = '' OR
@Unit IS NULL OR @Unit = '' OR
@Stroke IS NULL OR @Stroke = '' OR
@Fname IS NULL OR @Fname = ''
BEGIN
	RAISERROR('None of the given fields can be null', 14, 1)
	RETURN 1
END

DECLARE @Event_ID int
SET @Event_ID = (select EventID from [Event] Where Distance = @Distance AND
					Stroke = @Stroke AND Unit = @Unit)

DECLARE @Person_ID int
SET @Person_ID = (select ID from Person Where FName = @Fname AND LName = @Lname)

IF  (SELECT Count(SwimmerID)
	FROM [CompetesIn]
	WHERE (EventID = @Event_ID and SwimmerID = @Person_ID)) = 0
BEGIN
	RAISERROR('The CompetesIn listing given does not exist in the database.', 14, 2)
	RETURN 2
END

-- deleting into the table now with the appropriate details
DELETE [CompetesIn] 
WHERE ( EventID = @Event_ID AND SwimmerID = @Person_ID)

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