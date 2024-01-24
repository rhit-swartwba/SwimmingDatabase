USE [Swimming Information Database]
GO
ALTER PROCEDURE AddUser(
	@FName varchar(40),
	@LName varchar(40),
	@Sex char(1),
	@DOB date,
	@Height int,
	@Weight int,
	@Experience int,
	@Style varchar(20),
	@TeamName varchar(50),
	@Group varchar(50),
	@HoursPerWeek int,
	@Username nvarchar(50),
	@PasswordSalt varchar(50),
	@PasswordHash varchar(50),
	@AdminPass varchar(50)
)

AS
SET NOCOUNT ON

BEGIN

BEGIN TRANSACTION

-- Calling SQL statments here
DECLARE @Status1 smallint
DECLARE @Status2 smallint
DECLARE @Status3 smallint
DECLARE @Status4 smallint
DECLARE @Status5 smallint
SET @Status2 = 0
SET @Status3 = 0


EXEC @Status1 = insert_person @FName, @LName, @Sex, @DOB

DECLARE @PID int
SELECT @PID = ID FROM Person WHERE FName = @FName AND LName = @LName AND Sex = @Sex AND DOB = @DOB

if @Height != 0
BEGIN
	EXEC @Status2 = insert_swimmer @PID, @Height, @Weight
END

if @Experience = -1
BEGIN
	EXEC @Status3 = insert_coach @PID, @Experience, @Style
END

EXEC @Status4 = insert_partof @PID, @TeamName, @Group, @HoursPerWeek

EXEC @Status5 = Register @Username, @PasswordSalt, @PasswordHash



IF @Status1 != 0 OR @Status2 != 0 OR @Status3 != 0 OR @Status4 != 0 OR @Status5 != 0
BEGIN	
	RAISERROR('An error occured in completing this operation. Rolling back transaction.', 14, 1)
	ROLLBACK TRANSACTION
END

ELSE
BEGIN	
	COMMIT TRANSACTION
END

RETURN(0) 
END
