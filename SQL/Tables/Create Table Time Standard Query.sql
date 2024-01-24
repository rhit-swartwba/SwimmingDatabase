USE [Swimming Information Database]
GO

CREATE TABLE TimeStandard(
ID int identity(1,1),
EventID int, 
[Time] time,
[Level] varchar(20) check ([Level] = 'NCAA D3 Nationals' OR [Level] = 'Futures'),
Primary Key (ID),
Foreign Key (EventID) references Event(EventID)
on update cascade
)

drop table Achieved
drop table TimeStandard
