USE [Swimming Information Database]
GO

CREATE FUNCTION dbo.getPartOf (
@Fname_1 varchar(20),
@Lname_2 varchar(20),
@TeamName varchar(30)
)
RETURNS @getPersonInfo TABLE(
	PersonID int,
	TeamID int,
	[Group] varchar(50), 
	HoursPerWeek int
)
BEGIN

DECLARE @PID int
DECLARE @TID int
SELECT @PID = ID FROM Person WHERE FName = @Fname_1 and LName = @Lname_2
SELECT @TID = TeamID FROM Team WHERE TeamName = @TeamName

DECLARE @HoursPerWeek int
DECLARE @Group varchar(50)
SET @Group = (select [group] from PartOf where @PID = PersonID AND @TID = TeamID)
SET @HoursPerWeek = (select HoursPerWeek from PartOf where @PID = PersonID AND @TID = TeamID)

INSERT @getPersonInfo
Values(@PID, @TID, @Group, @HoursPerWeek)

RETURN 
END