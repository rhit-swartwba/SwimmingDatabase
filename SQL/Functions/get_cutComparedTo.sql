USE [Swimming Information Database]
GO

CREATE FUNCTION get_cutComparedTo (
@Fname_1 varchar(20),
@Lname_2 varchar(20),
@Cut_3 varchar(50)
)
RETURNS @get_cutComparedTo TABLE(
	Distance int,
	Stroke varchar(50),
	Unit varchar(50),
	[Time] Time,
	CutTime Time,
	TimeDiff int
)
BEGIN

DECLARE @PID int
SELECT @PID = ID
FROM Person
WHERE FName = @Fname_1 and LName = @Lname_2

DECLARE @Gender varchar(30)
SELECT @Gender = p.Sex
FROM Person p
WHERE p.ID = @PID

IF @Gender = 'M'
BEGIN 

INSERT @get_cutComparedTo
SELECT e.Distance, e.Stroke, e.Unit, ci.[Time], ts.MaleTime, DATEDIFF(MILLISECOND, ci.[Time], ts.MaleTime)
FROM TimeStandard ts
JOIN Event e on ts.EventID = e.EventID
JOIN CompetesIn ci ON ci.EventID = ts.eventID
WHERE ci.SwimmerID = @PID and ts.Level = @Cut_3

END 

ELSE
BEGIN 

INSERT @get_cutComparedTo
SELECT e.Distance, e.Stroke, e.Unit, ci.[Time], ts.FemaleTime, DATEDIFF(MILLISECOND, ci.[Time], ts.MaleTime)
FROM TimeStandard ts
JOIN Event e on ts.EventID = e.EventID
JOIN CompetesIn ci ON ci.EventID = ts.eventID
WHERE ci.SwimmerID = @PID and ts.Level = @Cut_3

END


RETURN 
END