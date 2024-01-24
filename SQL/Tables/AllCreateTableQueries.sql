USE [Swimming Information Database]
GO

CREATE TABLE TimeStandard(
ID int identity(1,1),
EventID int, 
[Time] time,
[Level] varchar(20) 
Constraint Level_Check check ([Level] IN ('NCAA D3 Nationals', 'Futures')),
Primary Key (ID),
Foreign Key (EventID) references Event(EventID)
on update cascade
)

Alter Table TimeStandard
	drop column [Time]
ALTER TABLE TimeStandard 
	Add MaleTime time,
	FemaleTime time

create table Swimmer (
	ID int primary key
	foreign key(ID) references Person(ID)
	on update cascade
	on delete cascade
	-- Table reserved for future swimmer data
)

ALTER TABLE Swimmer 
	Add Height int,
	[Weight] int


create table Person (
	ID int identity(1, 1) primary key,
	FName varchar(40),
	LName varchar(40) not null, 
	Sex char check (Sex in ('M', 'F')), 
	DOB date check (DOB < getdate())
);



CREATE TABLE CompetesIn (
EventID int,
SwimmerID int,
EquipmentModel varchar(50),
Foreign Key (EventID) references Event(EventID)
on update cascade,
Foreign Key (SwimmerID) references Swimmer(ID)
on update cascade
on delete cascade,
Foreign Key (EquipmentModel) references Equipment(Model)
on update cascade
)

ALTER TABLE CompetesIn 
	ADD [Time] time
	alter column EventID int not null
	alter column SwimmerID int not null
	Add Primary Key (EventID, SwimmerID)


create table Coach (
	ID int primary key
	foreign key references Person(ID)
	on update cascade
	on delete cascade,
	Experience int not null,
	Style varchar(20)
);


CREATE TABLE Achieved(
SwimmerID int,
TimeOff time,
TimeStandardID int,
Primary Key (SwimmerID, TimeStandardID),
Foreign Key (SwimmerID) References Person(ID)
ON DELETE cascade
ON UPDATE cascade,
Foreign Key (TimeStandardID) References TimeStandard(ID)
ON DELETE cascade
ON UPDATE cascade,
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

CREATE TABLE Equipment(
Model varchar(50),
Brand varchar(20) NOT NULL,
[Type] varchar(20) NOT NULL,
PRIMARY KEY(Model)
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

