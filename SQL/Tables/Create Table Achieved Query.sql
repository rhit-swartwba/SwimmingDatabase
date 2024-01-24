USE [Swimming Information Database]
GO

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