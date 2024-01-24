use [Swimming Information Database]
go

create table Person (
	ID int identity(1, 1) primary key,
	FName varchar(40),
	LName varchar(40) not null, 
	Sex char check (Sex in ('M', 'F')), 
	DOB date check (DOB < getdate())
);