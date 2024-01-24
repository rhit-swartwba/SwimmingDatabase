use [Swimming Information Database]
go

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
	
