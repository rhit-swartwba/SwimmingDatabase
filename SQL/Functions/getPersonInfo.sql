USE [Swimming Information Database]
GO


Create FUNCTION dbo.getPerson (
@Fname_1 varchar(20),
@Lname_2 varchar(20),
@isSwimmer bit,
@isCoach bit
)
RETURNS @getPersonInfo TABLE(
	FName varchar(50), 
	LName varchar(50), 
	Style varchar(50), 
	Experience int, 
	Height int, 
	[Weight] int
)
BEGIN

DECLARE @PID int
SELECT @PID = ID
FROM Person
WHERE FName = @Fname_1 and LName = @Lname_2


DECLARE @Height int, @Weight int
DECLARE @Style varchar(20), @Experience int

IF @isCoach = 1
BEGIN
SET @Style = (select style from Coach where @PID = ID)
SET @Experience = (select experience from coach where @PID = ID)
END

IF @isSwimmer = 1
BEGIN
SET @Height = (select height from Swimmer where @PID = ID)
SET @Weight = (select [weight] from swimmer where @PID = ID)
END

INSERT @getPersonInfo
Values(@Fname_1, @Lname_2, @Style, @Experience, @Height, @Weight)

RETURN 
END