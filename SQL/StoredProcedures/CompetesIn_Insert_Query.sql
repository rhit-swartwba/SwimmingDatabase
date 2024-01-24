Use [Swimming Information Database]
Go

CREATE PROCEDURE [dbo].[insert_CompetesIn]
(@FirstName varchar(20),
@LastName varchar(20),
@EquipmentModel varchar(50),
@EventDistance [int],
@Stroke varchar(15),
@Unit varchar(5),
@Time [time])
AS 
SET NOCOUNT ON

IF @FirstName IS NULL  OR @FirstName = '' OR
@LastName IS NULL  OR @LastName = '' OR
@EquipmentModel IS NULL  OR @EquipmentModel = '' OR
@EventDistance IS NULL  OR @EventDistance = '' OR
@Stroke IS NULL  OR @Stroke = '' OR
@Unit IS NULL  OR @Unit = '' OR
@Time IS NULL  OR @Time = ''
BEGIN
PRINT 'None of the input fields can be null or empty'
RETURN 1;
END

DECLARE @EventID_1 int
DECLARE @SwimmerID_2 int
SET @EventID_1 = (SELECT EventID FROM [Event] WHERE @EventDistance = Distance AND
	@Stroke = Stroke AND @Unit = Unit)
SET @SwimmerID_2 = (SELECT ID FROM [Person] WHERE @FirstName = FName AND
	@LastName = LName)

IF (SELECT Count(EventID) from [CompetesIn] WHERE EventID = @EventID_1 AND SwimmerID = @SwimmerID_2) > 0
BEGIN
	PRINT 'The EventID ' + CONVERT(varchar(30), @EventID_1 ) + ' and SwimmerID ' + CONVERT(varchar(30), @SwimmerID_2 )
	+ ' already exists.'
	RETURN 2
END
ELSE
BEGIN
INSERT INTO [CompetesIn] 
( [EventID], [SwimmerID], [EquipmentModel], [Time] )
VALUES ( @EventID_1, @SwimmerID_2, @EquipmentModel, @Time )
END
RETURN 0;