use [Swimming Information Database]
Go

create table Coach (
	ID int primary key
	foreign key references Person(ID)
	on update cascade
	on delete cascade,
	Experience int not null,
	Style varchar(20)
);