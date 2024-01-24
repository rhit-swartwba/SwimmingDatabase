Use [Swimming Information Database]
Go

Create Procedure update_CompetesIn(
@Fname varchar(20),
@Lname varchar(20),
@Distance int,
@Stroke varchar(15),
@Unit varchar(5),
@Time time,
@Model varchar(20)
)

AS
SET NOCOUNT ON

IF @Unit IS NULL OR @Unit = '' OR @Distance IS NULL OR @Distance = '' 
OR @Stroke IS NULL OR @Stroke = '' OR @Fname IS NULL OR @Fname = '' OR
@Lname IS NULL OR @Lname = '' OR @Model IS NULL OR @Model = '' or @Time IS NULL
OR @Time = ''
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN(1)
END


DECLARE @Event_ID int
SET @Event_ID = (Select EventID from [Event] Where Stroke = @Stroke AND Unit = @Unit
					AND Distance = @Distance)
DECLARE @PersonID int
SET @PersonID = (select ID from Person Where FName = @Fname AND LName = @Lname)


IF (SELECT Count(EventID)
	FROM [CompetesIn]
	WHERE (EventID = @Event_ID AND SwimmerID = @PersonID)) = 0
BEGIN
	RAISERROR('The CompetesIn entry selected does not exist in the database.', 14, 2)
	RETURN(2)
END


-- inserting into the table now with the appropriate details
Update [CompetesIn] 
SET [Time] = @Time, EquipmentModel = @Model 
WHERE EventID = @Event_ID AND [SwimmerID] = @PersonID

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