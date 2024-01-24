USE [Swimming Information Database]
GO

Create FUNCTION get_swimmerinfo (
@Fname_1 varchar(20),
@Lname_2 varchar(20)
)
RETURNS @get_swimmerinfo TABLE(
	FName varchar(50), 
	LName varchar(50), 
	Style varchar(50), 
	Experience int, 
	TeamName varchar(50),
	[Group] varchar(50),
	HoursPerWeek int,
	Height int,
	Weight int,
	Sex varchar(50),
	Age int
)
BEGIN

DECLARE @PID int
SELECT @PID = ID
FROM Person
WHERE FName = @Fname_1 and LName = @Lname_2

DECLARE @TID int
DECLARE @HPW int
DECLARE @GRP varchar(50)
DECLARE @Height int
DECLARE @Weight int
DECLARE @Sex varchar(50)
DECLARE @BirthDate Date
DECLARE @Age int
DECLARE @CurrentDate Date 
SET @CurrentDate = GETDATE()

SELECT @TID = TeamID, @GRP = pf.[Group], @HPW = pf.HoursPerWeek, @Height = s.Height, @Weight = s.Weight, @Sex = p.Sex, @BirthDate = p.DOB
FROM Swimmer s
JOIN PartOf pf on pf.PersonID = @PID
JOIN Person p on p.ID = @PID
WHERE s.ID = @PID


SELECT @Age = DATEDIFF(YY, @BirthDate, @CurrentDate) - CASE WHEN( (MONTH(@BirthDate)*100 + DAY(@BirthDate)) > (MONTH(@CurrentDate)*100 + DAY(@CurrentDate)) ) THEN 1 ELSE 0 END 

INSERT @get_swimmerinfo
SELECT p.FName, p.LName, c.Style, c.Experience, t.TeamName, @GRP, @HPW, @Height, @Weight, @Sex, @Age
FROM Coach c
JOIN Person p on c.ID = p.ID
JOIN PartOf pf on pf.TeamID = @TID and c.ID = pf.PersonID
JOIN Team t on t.TeamID = @TID

RETURN 
END