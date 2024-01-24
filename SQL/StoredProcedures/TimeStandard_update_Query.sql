Use [Swimming Information Database]
Go

Create Procedure [update_TimeStandard](
@Unit varchar(5),
@Distance int,
@Stroke varchar(15),
@Level varchar(15),
@MaleTime time = NULL,
@FemaleTime time = NULL
)

AS
SET NOCOUNT ON

IF @Unit IS NULL OR @Unit = '' OR @Distance IS NULL OR @Distance = '' 
OR @Stroke IS NULL OR @Stroke = '' OR @Level IS NULL OR @Level = ''
BEGIN
	RAISERROR('Level, Distance, Unit, and Stroke cannot be null or empty.', 14, 1)
	RETURN(1)
END


DECLARE @Event_ID int
SET @Event_ID = (Select EventID from [Event] Where Stroke = @Stroke AND Unit = @Unit
					AND Distance = @Distance)


IF (SELECT Count(EventID)
	FROM [TimeStandard]
	WHERE (EventID = @Event_ID AND [Level] = @Level)) = 0
BEGIN
	RAISERROR('The Time Standard selected does not exist in the database.', 14, 2)
	RETURN(2)
END


DECLARE @OldMaleTime time
DECLARE @OldFemaleTime time
SET @OldMaleTime = (Select MaleTime FROM TimeStandard WHERE EventID = @Event_ID 
				AND Level = @Level)
SET @OldFemaleTime = (Select FemaleTime FROM TimeStandard WHERE EventID = @Event_ID 
				AND Level = @Level)


IF @MaleTime IS NULL
BEGIN
SET @MaleTime = @OldMaleTime
END

IF @FemaleTime IS NULL
BEGIN
SET @FemaleTime = @OldFemaleTime
END

-- inserting into the table now with the appropriate details
Update [TimeStandard] 
SET [MaleTime] = @MaleTime, FemaleTime = @FemaleTime
WHERE EventID = @Event_ID AND [Level] = @Level

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