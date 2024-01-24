Use [Swimming Information Database]
Go

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

CREATE TABLE Equipment(
Model varchar(50),
Brand varchar(20) NOT NULL,
[Type] varchar(20) NOT NULL,
PRIMARY KEY(Model)
)

CREATE TABLE Event(
EventID int identity(1,1),
Distance int NOT NULL,
Stroke varchar(40) NOT NULL,
Unit varchar(10) NOT NULL,
PRIMARY KEY(EventID),
CHECK (Unit IN ('LCM', 'SCY', 'SCM')),
CHECK (Stroke IN ('Butterfly', 'Backstroke', 'Breaststroke', 'Freestyle', 'IM')),
CHECK (Distance > 0 and Distance <= 1650)
)
