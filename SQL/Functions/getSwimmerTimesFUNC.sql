USE [Swimming Information Database]
GO

CREATE FUNCTION [dbo].[get_swimmerTimes] (
@Fname_1 varchar(20),
@Lname_2 varchar(20),
@Distance_3 int = "-1",
@Stroke_4 varchar(50) = "All",
@Unit_5 varchar(50) = "All"
)
RETURNS @get_swimmerTimes TABLE(
	Distance int,
	Stroke varchar(50),
	Unit varchar(50),
	[Time] Time,
	Equip varchar(50),
	Brand varchar(50)
)
BEGIN

DECLARE @PID int
SELECT @PID = ID
FROM Person
WHERE FName = @Fname_1 and LName = @Lname_2


IF @Distance_3 = -1
BEGIN

INSERT @get_swimmerTimes
SELECT e.Distance, e.Stroke, e.Unit, ci.[Time], ci.EquipmentModel, eq.Brand
FROM Event e
JOIN CompetesIn ci on ci.SwimmerID = @PID
JOIN Equipment eq on eq.Model = ci.EquipmentModel

END

ELSE

BEGIN


DECLARE @EID int
SELECT @EID = EventID
FROM Event e
WHERE e.Distance = @Distance_3 AND e.Stroke = @Stroke_4 AND e.Unit = @Unit_5

INSERT @get_swimmerTimes
SELECT e.Distance, e.Stroke, e.Unit, ci.[Time], ci.EquipmentModel, eq.Brand
FROM Event e
JOIN CompetesIn ci on ci.SwimmerID = @PID
JOIN Equipment eq on eq.Model = ci.EquipmentModel
Where e.EventID = @EID

END

RETURN 
END