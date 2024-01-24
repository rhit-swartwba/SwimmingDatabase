Use [Swimming Information Database]
GO

CREATE PROCEDURE [dbo].[insert_TimeStandard_1]
(@Distance [int],
@Stroke varchar(15),
@Unit varchar(5),
@MaleTime time,
@FemaleTime time,
@Level varchar(20))
AS 

IF @Distance IS NULL  OR @Distance = '' OR
@Level IS NULL  OR @Level = '' OR
@Stroke IS NULL  OR @Stroke = '' OR
@Unit IS NULL  OR @Unit = '' OR
@MaleTime IS NULL  OR @MaleTime = ''OR
@FemaleTime is Null OR @FemaleTime = ''
BEGIN
PRINT 'None of the input fields can be null or empty'
RETURN 1;
END

DECLARE @EventID_1 int
SET @EventID_1 = (SELECT EventID FROM [Event] WHERE @Distance = Distance AND
	@Stroke = Stroke AND @Unit = Unit)

IF (SELECT Count(EventID) from [TimeStandard] WHERE EventID = @EventID_1 AND [Level] = @Level) > 0
BEGIN
	PRINT 'The TimeStandard already exists.'
	RETURN 2;
END
ELSE
BEGIN
INSERT INTO [TimeStandard] 
([EventID], [MaleTime], [FemaleTime], [Level])
VALUES (@EventID_1, @MaleTime, @FemaleTime, @Level)
END

RETURN 0;