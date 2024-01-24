USE [SID_TEST3]
GO
--TABLES 3
--FUNCTIONS 111
--STORED PROCEDURES 460
--TRIGGERS 1664
--VIEWS 1684
--INDEXES 1693
-- CREATE TABLES

CREATE TABLE [User] (
	Username varchar(50) NOT NULL,
	PasswordSalt varchar(50) NOT NULL,
	PasswordHash varchar(50) NOT NULL,
	PID int,
	PRIMARY KEY(Username)
)

create table Person (
	ID int identity(1, 1) primary key,
	FName varchar(40) not null,
	LName varchar(40) not null, 
	Sex char check (Sex in ('M', 'F')), 
	DOB date check (DOB < getdate())
)

create table Swimmer (
	ID int primary key,
	Height int,
	[Weight] int,
	foreign key(ID) references Person(ID)
	on update cascade
	on delete cascade
)

create table Coach (
	ID int primary key
	foreign key references Person(ID)
	on update cascade
	on delete cascade,
	Experience int not null,
	Style varchar(20) not null
)

CREATE TABLE [Event](
EventID int identity(1,1),
Distance int NOT NULL,
Stroke varchar(40) NOT NULL,
Unit varchar(10) NOT NULL,
PRIMARY KEY(EventID),
CHECK (Unit IN ('LCM', 'SCY', 'SCM')),
CHECK (Stroke IN ('Butterfly', 'Backstroke', 'Breaststroke', 'Freestyle', 'IM')),
CHECK (Distance > 0 and Distance <= 1650)
)

CREATE TABLE TimeStandard(
ID int identity(1,1),
EventID int, 
MaleTime decimal(7,2) NOT NULL,
FemaleTime decimal(7,2) NOT NULL,
[Level] varchar(20) NOT NULL 
Constraint Level_Check check ([Level] IN ('NCAA D3 A', 'Futures', 'NCAA D3 B')),
Constraint time_check check (MaleTime < 2000.00 AND FemaleTime < 2000.00),
Primary Key (ID),
Foreign Key (EventID) references Event(EventID)
on update cascade
)

CREATE TABLE Equipment(
Model varchar(50),
Brand varchar(20) NOT NULL,
[Type] varchar(20) NOT NULL,
PRIMARY KEY(Model)
)


CREATE TABLE CompetesIn (
EventID int not null,
SwimmerID int not null,
EquipmentModel varchar(50),
[Time] decimal(7,2),
constraint time_limit check([Time] < 2000.00),
Primary Key(EventID, SwimmerID),
Foreign Key (EventID) references Event(EventID)
on update cascade,
Foreign Key (SwimmerID) references Swimmer(ID)
on update cascade
on delete cascade,
Foreign Key (EquipmentModel) references Equipment(Model)
on update cascade
)



CREATE TABLE Team(
TeamID int identity(1,1),
TeamName varchar(50) NOT NULL,
Region varchar(10) NOT NULL,
PRIMARY KEY(TeamID),
UNIQUE(TeamName)
)

CREATE TABLE PartOf(
PersonID int,
TeamID int,
[Group] varchar(50),
HoursPerWeek int,
PRIMARY KEY(PersonID, TeamID),
FOREIGN KEY(TeamID) REFERENCES Team(TeamID)
on update cascade
on delete cascade,
FOREIGN KEY(PersonID) REFERENCES Person(ID)
on update cascade
on delete cascade,
CHECK (0 <= HoursPerWeek and HoursPerWeek <= 168),
)


Go


-- CREATION FUNCTIONS

CREATE FUNCTION get_cutComparedTo (
@PID int,
@Cut_3 varchar(50)
)
RETURNS @get_cutComparedTo TABLE(
	Distance int,
	Stroke varchar(50),
	Unit varchar(50),
	[Level] varchar(30),
	[Time] decimal(7,2),
	CutTime decimal(7,2),
	TimeDiff decimal(7,2)
)
BEGIN

DECLARE @Gender char
SELECT @Gender = p.Sex
FROM Person p
WHERE p.ID = @PID

IF @Gender = 'M'
BEGIN 

INSERT @get_cutComparedTo
SELECT e.Distance, e.Stroke, e.Unit, @Cut_3, ci.[Time], ts.MaleTime, ci.[Time]-ts.MaleTime
FROM TimeStandard ts
JOIN Event e on ts.EventID = e.EventID
JOIN CompetesIn ci ON ci.EventID = ts.eventID
WHERE ci.SwimmerID = @PID and ts.Level = @Cut_3

END 

ELSE
BEGIN 

INSERT @get_cutComparedTo
SELECT e.Distance, e.Stroke, e.Unit, @Cut_3, ci.[Time], ts.FemaleTime, ci.[Time]-ts.FemaleTime
FROM TimeStandard ts
JOIN Event e on ts.EventID = e.EventID
JOIN CompetesIn ci ON ci.EventID = ts.eventID
WHERE ci.SwimmerID = @PID and ts.Level = @Cut_3

END

RETURN 
END

Go

CREATE FUNCTION get_swimmerinfo (
@PID int
)
RETURNS @get_swimmerinfo TABLE(
	PID int,
	FName varchar(50), 
	LName varchar(50), 
	TeamName varchar(50),
	[Group] varchar(50),
	HoursPerWeek int,
	Height int,
	[Weight] int,
	Age int,
	AgeGroup varchar(20)
)
BEGIN


DECLARE @TeamName varchar(30)
DECLARE @HPW int
DECLARE @GRP varchar(50)
SELECT @TeamName = t.TeamName, @GRP = pf.[Group], @HPW = pf.HoursPerWeek
FROM PartOf pf
join Team t on pf.TeamID = t.TeamID
WHERE pf.PersonID = @PID

DECLARE @DOB date
SET @DOB = (select DOB from Person where ID = @PID)

DECLARE @Age int
SET @Age = CAST(datename(yy, getdate())AS INTEGER) - CAST(datename(yy, @DOB) AS INTEGER)

if datename(mm, getdate()) < datename(mm, @DOB)
begin
SET @AGE = @AGE - 1;
end
else if datename(mm, getdate()) = datename(mm, @DOB)
begin
if datename(dd, getdate()) < datename(dd, @DOB)
begin
SET @Age = @Age - 1;
end
end

DECLARE @AgeGroup varchar(20)
if @Age > 18
begin
set @AgeGroup = 'Collegiate'
end
else if @Age > 16
begin
set @AgeGroup = '17-18'
end
else if @Age > 14
begin
set @AgeGroup = '15-16'
end
else 
begin
set @AgeGroup = 'Age Group'
end

INSERT @get_swimmerinfo
SELECT @PID, p.FName, p.LName, @TeamName, @GRP, @HPW, s.Height, s.[Weight],
@Age, @AgeGroup

FROM Person p
JOIN Swimmer s on s.ID = p.ID
WHERE @PID = p.ID

RETURN 
END

Go

CREATE FUNCTION get_swimmerTimes (
@PID int,
@Distance_3 int = -1,
@Stroke_4 varchar(50) = 'All',
@Unit_5 varchar(50) = 'All'
)
RETURNS @get_swimmerTimes TABLE(
	Distance int,
	Stroke varchar(50),
	Unit varchar(50),
	[Time] decimal(6,2),
	Equip varchar(50),
	Brand varchar(50)
)
BEGIN


IF @Distance_3 = -1
BEGIN

INSERT @get_swimmerTimes
SELECT e.Distance, e.Stroke, e.Unit, ci.[Time], ci.EquipmentModel, eq.Brand
FROM [Event] e
JOIN CompetesIn ci on ci.EventID = e.EventID
JOIN Equipment eq on eq.Model = ci.EquipmentModel
WHERE ci.SwimmerID = @PID
ORDER BY Stroke, Distance desc

END

ELSE
BEGIN

DECLARE @EID int
SELECT @EID = EventID
FROM Event e
WHERE e.Distance = @Distance_3 AND e.Stroke = @Stroke_4 AND e.Unit = @Unit_5

INSERT @get_swimmerTimes
SELECT @Distance_3, @Stroke_4, @Unit_5, ci.[Time], ci.EquipmentModel, eq.Brand
FROM CompetesIn ci
JOIN Equipment eq on eq.Model = ci.EquipmentModel
Where ci.EventID = @EID AND ci.SwimmerID = @PID

END

RETURN 
END

Go

CREATE function getCoachInfo(
@CID int
)

returns @getCoachInfo table(
FName varchar(20),
LName varchar(20),
teamName varchar(30),
experience int,
style varchar(20),
[group] varchar (20),
hoursPerWeek int
)

as
begin

insert @getCoachInfo
select teamName = p.FName, p.LName,(select TeamName from Team where po.TeamID = TeamID), 
					c.experience, c.style, po.[group], po.hoursPerWeek
from Coach c join PartOf po on c.ID = po.PersonID
join Person p on p.ID = c.ID
where @CID = c.ID

return
end

Go


CREATE FUNCTION getPartOf (
@PID int,
@TeamName varchar(30)
)
RETURNS @getPersonInfo TABLE(
	PersonID int,
	TeamID int,
	[Group] varchar(50), 
	HoursPerWeek int
)
BEGIN

DECLARE @TID int
SELECT @TID = TeamID FROM Team WHERE TeamName = @TeamName

DECLARE @HoursPerWeek int
DECLARE @Group varchar(50)
SET @Group = (select [group] from PartOf where @PID = PersonID AND @TID = TeamID)
SET @HoursPerWeek = (select HoursPerWeek from PartOf where @PID = PersonID AND @TID = TeamID)

INSERT @getPersonInfo
Values(@PID, @TID, @Group, @HoursPerWeek)

RETURN 
END

Go

CREATE FUNCTION getPerson (
@Fname_1 varchar(20),
@Lname_2 varchar(20),
@isSwimmer bit,
@isCoach bit
)
RETURNS @getPersonInfo TABLE(
	FName varchar(50), 
	LName varchar(50),
	PersonID int,
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
Values(@Fname_1, @Lname_2, @PID, @Style, @Experience, @Height, @Weight)

RETURN 
END

Go

CREATE function getTimeOffCut(
@PID int, 
@Distance int,
@Stroke varchar(20),
@Unit varchar(5),
@Level varchar(30)
)
returns @getTimeOffCut table(
Distance int,
Stroke varchar(20), 
Unit varchar(5),
[Level] varchar(30),
[Time] decimal(7,2),
CutTime decimal(7,2),
TimeOff decimal(7,2)
)
as
begin
declare @EID int
set @EID = (select EventID from [Event] where @Distance = Distance AND @Unit = Unit AND @Stroke = Stroke)

declare @currentTime decimal(7,2)
set @currentTime = (select time from CompetesIn where @PID = SwimmerID AND @EID = EventID)

declare @sex Nvarchar(1)
set @sex = (select sex from person where @PID = ID)

declare @cutTime decimal(7,2)
if @sex = 'M'
begin
set @cutTime = (select MaleTime from TimeStandard where @EID = EventID AND @Level = [Level])
end
else
begin
set @cutTime = (select FemaleTime from TimeStandard where @EID = EventID AND @Level = [Level])
end

insert @getTimeOffCut 
select @Distance, @Stroke, @Unit, @Level, @currentTime, @cutTime, @currentTime - @cutTime

return
end

Go


CREATE function getTimeStandardInfo(
@Distance int,
@Stroke varchar(20),
@Unit varchar(5),
@Level varchar(30) )

returns @getTimeStandardInfo table(
MaleTime decimal(6,2),
FemaleTime decimal(6,2))
as
begin

declare @EID int
set @EID = (select EventID from [Event] 
					where @Distance = Distance AND @Stroke = Stroke AND @Unit = Unit)

insert @getTimeStandardInfo
select MaleTime, FemaleTime from TimeStandard 
where EventID = @EID
AND [Level] = @Level
return
end

Go


Create function getAllSuitModels()
returns table
as
return(
select Model from Equipment)


GO


Create function getAllTeamNames()
returns table
as

return(
select distinct TeamName from Team)


go

Create function getAllCutLevels()
returns table
as

return(
select distinct [Level] from TimeStandard)


GO



create function getTeamInfo(
@PID int)

returns table
as
return(
select  t.TeamName, po.[Group], po.HoursPerWeek from PartOf po
join Team t on t.TeamID = po.TeamID
where @PID = po.PersonID
)


GO


-- STORED PROCEDURES

Create Procedure update_TimeStandard(
@Distance int,
@Stroke varchar(15),
@Unit varchar(5),
@MaleTime decimal(7,2) = NULL,
@FemaleTime decimal(7,2) = NULL,
@Level varchar(15)
)

AS
SET NOCOUNT ON
begin

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


DECLARE @OldMaleTime decimal(7,2)
DECLARE @OldFemaleTime decimal(7,2)
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

RETURN 0
END

Go


CREATE PROCEDURE update_team (
@TeamName_input varchar(50),
@Region_input varchar(10)
)

AS
SET NOCOUNT ON
begin
-- Making sure the OrderID and ProductID are valid
IF @TeamName_input IS NULL OR @TeamName_input = '' OR @Region_input IS NULL OR @Region_input = ''
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN 1
END

IF (SELECT Count(TeamName)
	FROM [Team]
	WHERE (TeamName = @TeamName_input)) = 0
BEGIN
	RAISERROR('The given team information does not exist in the database.', 14, 2)
	RETURN 2 
END

DECLARE @teamIDVal int
SET @teamIDVal = (select TeamID from Team where TeamName = @TeamName_input)

-- inserting into the table now with the appropriate details
Update [Team] 
Set TeamName = @TeamName_input, Region = @Region_input
Where TeamID = @teamIDVal


RETURN 0 
END

Go

CREATE PROCEDURE update_swimmer (
@PersonID int,
@Height_input int,
@Weight_input int
)

AS
SET NOCOUNT ON
begin

IF @PersonID IS NULL
BEGIN
	RAISERROR('ID cannot be null.', 14, 1)
	RETURN 1
END

IF (SELECT Count(ID) from Person where @PersonID=ID) = 0 BEGIN
	RAISERROR('The specified person does not exist in the database.', 14, 2)
	RETURN 2
END

IF (SELECT Count(ID) from Swimmer where @PersonID=ID) = 0 BEGIN
	RAISERROR('The specified person is not a swimmer.', 14, 3)
	RETURN 3
END

-- inserting into the table now with the appropriate details
UPDATE Swimmer 
SET Height=@Height_input, Weight=@Weight_input
WHERE @PersonID = ID


RETURN 0
END

Go

CREATE PROCEDURE update_person (
@PersonID int,
@FName_input varchar(40),
@LName_input varchar(40),
@Sex_input char(1),
@DOB_input date
)

AS
SET NOCOUNT ON
begin
IF @PersonID IS NULL OR @FName_input = '' OR @LName_input IS NULL OR @LName_input = '' 
OR @LName_input IS NULL OR @Sex_input = '' OR @Sex_input IS NULL OR @DOB_input IS NULL
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN 1
END

IF @Sex_input not in ('M', 'F') BEGIN
	RAISERROR('The specified sex is not valid.', 14, 3) 
	RETURN 2
END

IF (SELECT Count(ID) from Person where @PersonID=ID) = 0 BEGIN
	RAISERROR('The specified person does not exist in the database.', 14, 2)
	RETURN 3
END

-- inserting into the table now with the appropriate details
UPDATE Person 
SET FName=@FName_input, LName=@LName_input, Sex=@Sex_input, DOB=@DOB_input
WHERE @PersonID = ID


RETURN 0 
END

Go

CREATE PROCEDURE update_partof (
@PID int,
@Group_4 varchar(50),
@HoursPerWeek_5 int,
@OldTeamID int
)

AS
SET NOCOUNT ON
BEGIN

IF @PID IS NULL OR @OldTeamID IS NULL
OR @Group_4 IS NULL OR @Group_4 = ''
OR @HoursPerWeek_5 IS NULL OR @HoursPerWeek_5 = ''
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN(1) 
END

-- inserting into the table now with the appropriate details
Update [PartOf] 
SET [Group] = @Group_4, HoursPerWeek = @HoursPerWeek_5
WHERE PersonID = @PID AND TeamID = @OldTeamID

RETURN(0)

END

Go


CREATE PROCEDURE update_equipment (
@model_1 varchar(50),
@Brand_2 varchar(20),
@Type_3 varchar(20)
)

AS
SET NOCOUNT ON
begin
IF @model_1 IS NULL OR @model_1 = '' OR @brand_2 IS NULL OR @brand_2 = '' 
OR @Type_3 IS NULL OR @Type_3 = ''
BEGIN
	RAISERROR('None of the fields can be null or empty', 14, 1)
	RETURN(1)
END

IF  (SELECT Count(@model_1)
	FROM [Equipment]
	WHERE (model = @model_1)) = 0

BEGIN
	RAISERROR('The given equipment value does not exist in the database.', 14, 2)
	RETURN(2)
END

-- inserting into the table now with the appropriate details
Update [Equipment] 
SET brand = @Brand_2, type = @Type_3
WHERE model = @model_1

	
RETURN(0)
END


Go

CREATE Procedure update_CompetesIn(
@PID int,
@Distance int,
@Stroke varchar(15),
@Unit varchar(5),
@Time decimal(7,2),
@Model varchar(20)
)

AS
SET NOCOUNT ON

BEGIN

IF @Unit IS NULL OR @Unit = '' OR @Distance IS NULL OR @Distance = '' 
OR @Stroke IS NULL OR @Stroke = '' OR @PID IS NULL OR @Model IS NULL OR @Model = '' or @Time IS NULL
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN(1)
END


DECLARE @Event_ID int
SET @Event_ID = (Select EventID from [Event] Where Stroke = @Stroke AND Unit = @Unit
					AND Distance = @Distance)


IF (SELECT Count(EventID)
	FROM [CompetesIn]
	WHERE (EventID = @Event_ID AND SwimmerID = @PID)) = 0
BEGIN
	RAISERROR('The CompetesIn entry selected does not exist in the database.', 14, 2)
	RETURN(2)
END

Update [CompetesIn] 
SET [Time] = @Time, EquipmentModel = @Model 
WHERE EventID = @Event_ID AND [SwimmerID] = @PID

RETURN(0)

END

Go


CREATE PROCEDURE update_coach (
@PersonID int,
@Experience_input int,
@Style_input varchar(20)
)

AS
SET NOCOUNT ON
begin
IF @PersonID IS NULL OR @Experience_input IS NULL
BEGIN
	RAISERROR('Experience and ID cannot be null.', 14, 1)
	RETURN 1
END

IF (SELECT Count(ID) from Person where @PersonID=ID) = 0 BEGIN
	RAISERROR('The specified person does not exist in the database.', 14, 2)
	RETURN 2
END

IF (SELECT Count(ID) from Coach where @PersonID=ID) = 0 BEGIN
	RAISERROR('The specified person is not a coach.', 14, 3)
	RETURN 3
END

-- inserting into the table now with the appropriate details
UPDATE Coach 
SET Experience=@Experience_input, Style=@Style_input
WHERE @PersonID = ID


RETURN(0)
END


Go

CREATE PROCEDURE Register(
	@Username nvarchar(50),
	@PasswordSalt varchar(50),
	@PasswordHash varchar(50),
	@PID int
)
AS
BEGIN
	if @Username is null or @Username = ''
	BEGIN
		Print 'Username cannot be null or empty.';
		RETURN (1)
	END
	if @PasswordSalt is null or @PasswordSalt = ''
	BEGIN
		Print 'PasswordSalt cannot be null or empty.';
		RETURN (2)
	END
	if @PasswordHash is null or @PasswordHash = ''
	BEGIN
		Print 'PasswordHash cannot be null or empty.';
		RETURN (3)
	END
	IF (SELECT COUNT(*) FROM [User]
          WHERE Username = @Username) = 1
	BEGIN
      PRINT 'ERROR: Username already exists.';
	  RETURN(4)
	END
	INSERT INTO [User](Username, PasswordSalt, PasswordHash, PID)
	VALUES (@username, @passwordSalt, @passwordHash, @PID)
return 0
END

Go


CREATE procedure isCoach(
@PID int)
as
begin
if @PID is NULL
begin
raiserror('The Person ID cannot be Null', 14, 1)
return 3;
end
if @PID in (select ID from Coach)
begin
return 1;
end
else
begin
return 2;
end
end


Go

CREATE PROCEDURE insert_TimeStandard
(@Distance int,
@Stroke varchar(15),
@Unit varchar(5),
@MaleTime decimal(7,2),
@FemaleTime decimal(7,2),
@Level varchar(20))
AS 
BEGIN

IF @Distance IS NULL  OR @Distance = '' OR
@Level IS NULL  OR @Level = '' OR
@Stroke IS NULL  OR @Stroke = '' OR
@Unit IS NULL  OR @Unit = '' OR
@MaleTime IS NULL  OR
@FemaleTime is Null
BEGIN
PRINT 'None of the input fields can be null or empty'
RETURN(1)
END

DECLARE @EventID_1 int
SET @EventID_1 = (SELECT EventID FROM [Event] WHERE @Distance = Distance AND
	@Stroke = Stroke AND @Unit = Unit)

IF (SELECT Count(EventID) from [TimeStandard] WHERE EventID = @EventID_1 AND [Level] = @Level) > 0
BEGIN
	PRINT 'The TimeStandard already exists.'
	RETURN(2)
END
ELSE
BEGIN
INSERT INTO [TimeStandard] 
([EventID], [MaleTime], [FemaleTime], [Level])
VALUES (@EventID_1, @MaleTime, @FemaleTime, @Level)
END

RETURN(0)

END


Go

CREATE PROCEDURE insert_team (
@TeamName_input varchar(50),
@Region_input varchar(10)
)

AS
SET NOCOUNT ON
begin
-- Making sure the OrderID and ProductID are valid
IF @TeamName_input IS NULL OR @TeamName_input = '' OR @Region_input IS NULL OR @Region_input = ''
BEGIN
	RAISERROR('None of the given values can be null or empty.', 14, 1)
	RETURN(1)
END

IF (SELECT Count(TeamName)
	FROM [Team]
	WHERE (TeamName = @TeamName_input)) > 0
BEGIN
	RAISERROR('The given team already exists in the database.', 14, 2)
	RETURN(2)
END


-- inserting into the table now with the appropriate details
INSERT INTO [Team] 
 ([TeamName], [Region])
VALUES (@TeamName_input, @Region_input)


RETURN(0)
END

Go

Create procedure insert_swimmer(
	@PersonID int,
	@Height int,
	@Weight int)
as
SET NOCOUNT ON
BEGIN

	if @PersonID is NULL OR @PersonID = ''
	begin
	raiserror ('The PID cannot be null', 15, 1)
	return (1)
	end

	if (SELECT COUNT(ID) FROM Swimmer WHERE ID = @PersonID) > 0
	BEGIN
		raiserror('Swimmer is already in the database.', 14, 2)
		RETURN(2)
	end
	
	insert into Swimmer (ID, Height, [Weight])
	values (@PersonID, @Height, @Weight)
	
RETURN(0)

END

Go


CREATE procedure insert_person(
	@FName varchar(40),
	@LName varchar(40),
	@Sex char(1),
	@DOB date)
as
BEGIN
	if @LName is null or @LName = '' OR @FName is null or @FName = '' OR @DOB is null  begin
		raiserror('InsertPerson: None of the given fields can be null.', 14, 1)
		RETURN(1)
	end

	--we used a trigger to determine if a person is already in the database

	insert into Person (FName, LName, Sex, DOB)
	values (@FName, @LName, @Sex, @DOB)

RETURN(0)
END

Go


CREATE PROCEDURE insert_partof (
@PID_1 int,
@TeamName_2 varchar(50),
@Group_3 varchar(50),
@HoursPerWeek_4 int
)

AS
SET NOCOUNT ON
begin

IF @PID_1 IS NULL OR @TeamName_2 = '' OR @TeamName_2 IS NULL OR @Group_3 = '' OR @Group_3 IS NULL OR @HoursPerWeek_4 = '' OR @HoursPerWeek_4 IS NULL
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN 1
END

DECLARE @TID int
SELECT @TID = TeamID FROM Team WHERE TeamName = @TeamName_2

IF @TID IS NULL BEGIN
	RAISERROR('The specified team does not exist.', 14, 3)
	RETURN 3
END

IF (SELECT Count(PersonID)
	FROM [PartOf]
	WHERE (PersonID = @PID_1 AND TeamID = @TID)) > 0
BEGIN
	RAISERROR('The swimmer is already on the team in the database.', 14, 2)
	RETURN 2
END

-- inserting into the table now with the appropriate details
INSERT INTO [PartOF] 
( [PersonID], [TeamID], [Group], [HoursPerWeek])
VALUES ( @PID_1, @TID, @Group_3, @HoursPerWeek_4)


RETURN(0)
END


Go


CREATE PROCEDURE insert_event (
@Distance_1 int,
@Stroke_2 varchar(40),
@Unit_3 varchar(10)
)

AS
SET NOCOUNT ON
begin

-- Making sure the OrderID and ProductID are valid
IF @Distance_1 IS NULL OR @Distance_1 = '' OR @Stroke_2 IS NULL OR @Stroke_2 = ''
   OR @Unit_3 IS NULL OR @Unit_3 = ''
BEGIN
	RAISERROR('None of the given values can be null or empty.', 14, 1)
	RETURN(1)
END

DECLARE @EveID int
SELECT @EveID = EventID FROM Event WHERE Distance = @Distance_1 AND Unit = @Unit_3 AND Stroke = @Stroke_2

IF @EveID != 0
BEGIN
	RAISERROR('The given event already exists in the database.', 14, 2)
	RETURN(2)
END


-- inserting into the table now with the appropriate details
INSERT INTO [Event] 
 ([Distance], [Stroke], [Unit])
VALUES (@Distance_1, @Stroke_2, @Unit_3)


RETURN(0)
END


Go

CREATE PROCEDURE insert_equipment (
@model_1 varchar(50),
@Brand_2 varchar(20),
@Type_3 varchar(20)
)

AS
SET NOCOUNT ON
begin
IF @model_1 IS NULL OR @model_1 = '' OR @brand_2 IS NULL OR @brand_2 = '' 
OR @Type_3 IS NULL OR @Type_3 = ''
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN(1)
END

IF  (SELECT Count(@model_1)
	FROM [Equipment]
	WHERE (model = @model_1)) > 0
BEGIN
	RAISERROR('The given equipment already exists in the database.', 14, 2)
	RETURN(2)
END

-- inserting into the table now with the appropriate details
INSERT INTO [Equipment] 
( [Model], [Brand], [Type])
VALUES ( @Model_1, @Brand_2, @Type_3)


RETURN(0)
END


Go

CREATE PROCEDURE insert_CompetesIn
(@PID int,
@EquipmentModel varchar(50),
@EventDistance int,
@Stroke varchar(15),
@Unit varchar(5),
@Time decimal(7,2))
AS 
SET NOCOUNT ON

BEGIN

IF @PID is NULL OR
@EquipmentModel IS NULL  OR @EquipmentModel = '' OR
@EventDistance IS NULL  OR @EventDistance = '' OR
@Stroke IS NULL  OR @Stroke = '' OR
@Unit IS NULL  OR @Unit = '' OR
@Time IS NULL
BEGIN
PRINT 'None of the input fields can be null or empty'
RETURN(1);
END

DECLARE @EventID_1 int
SET @EventID_1 = (SELECT EventID FROM [Event] WHERE @EventDistance = Distance AND
	@Stroke = Stroke AND @Unit = Unit)

IF @EventID_1 IS NULL BEGIN
	PRINT 'The specified event does not exist.'
	RETURN(3)
END

IF (SELECT Count(EventID) from [CompetesIn] WHERE EventID = @EventID_1 AND SwimmerID = @PID) > 0
BEGIN
	PRINT 'The EventID ' + CONVERT(varchar(30), @EventID_1 ) + ' and SwimmerID ' + CONVERT(varchar(30), @PID )
	+ ' already exists.'
	RETURN(2)
END
ELSE
BEGIN
INSERT INTO [CompetesIn] 
( [EventID], [SwimmerID], [EquipmentModel], [Time] )
VALUES ( @EventID_1, @PID, @EquipmentModel, @Time )
END
RETURN(0)

END


Go

CREATE procedure insert_coach(
	@PersonID int,
	@Experience int,
	@Style varchar(20))
as
SET NOCOUNT ON

BEGIN

	if @Experience = NULL OR @Style = NULL OR (SELECT Count(ID) From Person WHERE ID = @PersonID) = 0
	BEGIN
		raiserror('The person does not exist in the database or the corresponding fields are empty.', 14, 1)
		RETURN(1)
	END
	
	insert into Coach (ID, Experience, Style)
	values (@PersonID, @Experience, @Style)

RETURN(0)

END

Go

CREATE PROCEDURE getPersonID (
@FirstName varchar(50),
@LastName varchar(50),
@PID int OUTPUT
)

AS
SET NOCOUNT ON

BEGIN

IF (SELECT Count(ID) From Person WHERE fName = @FirstName AND lName = @LastName) = 0
BEGIN
	Raiserror('This person does not exist in the database', 14, 1)
	RETURN(1)
END

SELECT @PID = ID FROM PERSON WHERE fName = @FirstName AND lName = @LastName
RETURN(0)

END



Go

CREATE Procedure delete_TimeStandard (
@Distance int,
@Unit varchar(3),
@Stroke varchar(15),
@Level varchar(20)
)

AS
SET NOCOUNT ON
BEGIN

IF @Distance IS NULL OR @Distance = '' OR
@Unit IS NULL OR @Unit = '' OR
@Stroke IS NULL OR @Stroke = '' OR
@Level IS NULL OR @Level = ''
BEGIN
	RAISERROR('None of the given fields can be null', 14, 1)
	RETURN 1
END

DECLARE @Event_ID int
SET @Event_ID = (select EventID from [Event] Where Distance = @Distance AND
					Stroke = @Stroke AND Unit = @Unit)

IF  (SELECT Count(@Event_ID)
	FROM [TimeStandard]
	WHERE (EventID = @Event_ID AND [Level] = @Level)) = 0
BEGIN
	RAISERROR('The TimeStandard given does not exist in the database.', 14, 2)
	RETURN 2
END

-- deleting into the table now with the appropriate details
DELETE [TimeStandard] 
WHERE ( EventID = @Event_ID AND [Level] = @Level)


RETURN(0)
END




Go

CREATE PROCEDURE delete_team (
@TeamName_input varchar(50)
)

AS
SET NOCOUNT ON
BEGIN

-- Making sure the OrderID and ProductID are valid
IF @TeamName_input IS NULL OR @TeamName_input = ''
BEGIN
	RAISERROR('None of the given fields can be null or empty', 14, 1)
	RETURN(1)
END

IF (SELECT Count(TeamName)
	FROM [Team]
	WHERE (TeamName = @TeamName_input)) = 0
BEGIN
	RAISERROR('The given team does not exist in the database.', 14, 2)
	RETURN(2)
END

-- inserting into the table now with the appropriate details
DELETE [Team] 
WHERE ( [TeamName] = @TeamName_input)

RETURN(0)
END



Go


CREATE PROCEDURE delete_swimmer (
@PersonID int
)

AS
SET NOCOUNT ON
BEGIN
IF @PersonID IS NULL
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN
END

IF (SELECT Count(ID) from Person where @PersonID=ID) = 0 BEGIN
	RAISERROR('The specified person does not exist in the database.', 14, 2)
	RETURN
END

IF (SELECT Count(ID) from Swimmer where @PersonID=ID) = 0 BEGIN
	RAISERROR('The specified person is not a swimmer.', 14, 3)
	RETURN
END

DELETE Person 
WHERE ID = @PersonID

RETURN(0)
END



Go


CREATE PROCEDURE delete_person (
@PersonID int
)

AS
SET NOCOUNT ON

BEGIN

IF @PersonID = NULL
BEGIN
	RAISERROR('None of the fields can be null or empty',14, 1)
	RETURN(1)
END

IF (SELECT Count(ID) From Person Where ID = @PersonID) = 0
BEGIN
	RAISERROR('The person does not exist in the database.', 14, 2)
	RETURN(2)
END

DELETE Person 
WHERE ID = @PersonID

return 0
END

Go




CREATE PROCEDURE delete_partof (
@PID int,
@TeamName_3 varchar(50)
)

AS
SET NOCOUNT ON
BEGIN

IF @PID IS NULL
OR @TeamName_3 IS NULL OR @TeamName_3 = ''
BEGIN
	RAISERROR('None of the given fields can be null or empty', 14, 1)
	RETURN(1)
END

DECLARE @TID int
SELECT @TID = TeamID FROM Team WHERE TeamName = @TeamName_3

IF (SELECT Count(PersonID)
	FROM [PartOf]
	WHERE (PersonID = @PID AND TeamID = @TID)) = 0
BEGIN
	RAISERROR('The swimmer is not currently on the team in the database.', 14, 2)
	RETURN(2)
END

-- deleting into the table now with the appropriate details
DELETE [PartOf] 
WHERE (PersonID = @PID AND TeamID = @TID)

RETURN(0)

END

Go




CREATE PROCEDURE delete_equipment (
@model_1 varchar(50)
)

AS
SET NOCOUNT ON
BEGIN

IF @model_1 IS NULL OR @model_1 = ''
BEGIN
	RAISERROR('None of the given fields can be null', 14, 1)
	RETURN(1)
END

IF (SELECT Count(@model_1)
	FROM [Equipment]
	WHERE (model = @model_1)) = 0
BEGIN
	RAISERROR('The equipment given does not exist in the database.', 14, 2)
	RETURN(2)
END

-- deleting into the table now with the appropriate details
DELETE [Equipment] 
WHERE ( model = @model_1)
	
RETURN(0)
END

Go




CREATE Procedure delete_CompetesIn (
@PID int,
@Distance int,
@Unit varchar(3),
@Stroke varchar(15)
)

AS
SET NOCOUNT ON

BEGIN

IF @Distance IS NULL OR @Distance = '' OR
@Unit IS NULL OR @Unit = '' OR
@Stroke IS NULL OR @Stroke = '' OR
@PID IS NULL
BEGIN
	RAISERROR('None of the given fields can be null', 14, 1)
	RETURN(1)
END

DECLARE @Event_ID int
SET @Event_ID = (select EventID from [Event] Where Distance = @Distance AND
					Stroke = @Stroke AND Unit = @Unit)

IF  (SELECT Count(SwimmerID)
	FROM [CompetesIn]
	WHERE (EventID = @Event_ID and SwimmerID = @PID)) = 0
BEGIN
	RAISERROR('The CompetesIn listing given does not exist in the database.', 14, 2)
	RETURN(2)
END

-- deleting into the table now with the appropriate details
DELETE [CompetesIn] 
WHERE ( EventID = @Event_ID AND SwimmerID = @PID)

RETURN(0)

END

Go




CREATE PROCEDURE delete_coach(
@PersonID int
)

AS
SET NOCOUNT ON
BEGIN

IF @PersonID IS NULL
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN 1
END

IF (SELECT Count(ID) from Person where @PersonID=ID) = 0 BEGIN
	RAISERROR('The specified person does not exist in the database.', 14, 2)
	RETURN 2
END

IF (SELECT Count(ID) from Coach where @PersonID=ID) = 0 BEGIN
	RAISERROR('The specified person is not a coach.', 14, 3)
	RETURN 3
END

DELETE Person 
WHERE ID = @PersonID

	
RETURN(0)
END
Go




CREATE PROCEDURE AddUser(
	@FName1 varchar(40),
	@LName2 varchar(40),
	@Sex3 char(1),
	@DOB4 date,
	@Height5 int,
	@Weight6 int,
	@Experience7 int,
	@Style8 varchar(20),
	@TeamName9 varchar(50),
	@Group10 varchar(50),
	@HoursPerWeek11 int,
	@Username12 nvarchar(50),
	@PasswordSalt13 varchar(50),
	@PasswordHash14 varchar(50)
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


EXEC @Status1 = insert_person @FName1, @LName2, @Sex3, @DOB4
IF @Status1 != 0
BEGIN	
	RAISERROR('An error occured in completing this operation. Rolling back transaction.', 14, 1)
	ROLLBACK TRANSACTION
END

DECLARE @PID0 int
SELECT @PID0 = ID FROM Person WHERE FName = @FName1 AND LName = @LName2 AND Sex = @Sex3 AND DOB = @DOB4


if @Height5 != 0
BEGIN
	EXEC @Status2 = insert_swimmer @PID0, @Height5, @Weight6
END

if @Experience7 != -1
BEGIN
	EXEC @Status3 = insert_coach @PID0, @Experience7, @Style8
END

EXEC @Status4 = insert_partof @PID_1 = @PID0, @TeamName_2 = @TeamName9, @Group_3 = @Group10 , @HoursPerWeek_4 = @HoursPerWeek11

EXEC @Status5 = Register @Username12, @PasswordSalt13, @PasswordHash14, @PID0


IF @Status2 != 0 OR @Status3 != 0 OR @Status4 != 0 OR @Status5 != 0
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

Go




CREATE PROCEDURE addToDatabase(
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
EXEC insert_person @FName, @LName, @Sex, @DOB

DECLARE @PID int
SELECT @PID = ID FROM Person WHERE FName = @FName AND LName = @LName AND Sex = @Sex AND DOB = @DOB

if @Height != 0
BEGIN
	EXEC insert_swimmer @PID, @Height, @Weight
END

if @Experience = -1
BEGIN
	EXEC insert_coach @PID, @Experience, @Style
END

EXEC insert_partof @PID, @TeamName, @Group, @HoursPerWeek

EXEC Register @Username, @PasswordSalt, @PasswordHash, @PID


DECLARE @Status SMALLINT
SET @Status = @@ERROR
IF @Status <> 0 
BEGIN	
	RAISERROR('An error occured in completing this operation', 14, @status)
	ROLLBACK TRANSACTION
END

ELSE
BEGIN	
	COMMIT TRANSACTION
END

END



--Trigger
go

create trigger noDuplicatePeople on Person
after insert
as

declare @newFName varchar(40)
declare @newLName varchar(40)
set @newFName = (select FName from inserted)
set @newLName = (select LName from inserted)

if(select Count(FName) from Person where FName = @newFName AND LName = @newLName) > 1
begin
rollback transaction
end


--View

GO


create view viewTimeStandards
as
select ts.[level], e.Distance, e.Stroke, e.Unit, ts.MaleTime, ts.FemaleTime
from TimeStandard ts
join [Event] e on e.EventID = ts.EventID

GO

--Index
create nonclustered index  Name_Search on Person(FName, LName)
