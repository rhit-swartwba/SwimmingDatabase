use [Swimming Information Database]
go

create trigger noDuplicatePeople on Person
after insert
as

declare @newFName varchar(40)
set @newFName = (select FName from inserted)
declare @newLName varchar(40)
set @newLName = (select LName from inserted)

if(select Count(FName) from Person where FName = @newFName AND LName = @newLName) > 1
begin
rollback transaction
end