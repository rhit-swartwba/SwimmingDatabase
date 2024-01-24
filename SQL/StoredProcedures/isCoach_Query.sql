use [Swimming Information Database]
go

create procedure isCoach(
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