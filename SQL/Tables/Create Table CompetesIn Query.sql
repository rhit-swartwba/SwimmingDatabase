USE [Swimming Information Database]
GO

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