use [Swimming Information Database]
go

create function dbo.getCoachInfo(
@firstName varchar(30),
@lastName varchar(30)
)

returns @getCoachInfo table(
teamName varchar(30),
experience int,
style varchar(20),
[group] varchar (20),
hoursPerWeek int
)

as
begin

declare @CID int
set @CID = (select ID from Person where FName = @firstName AND LName = @lastName)

insert @getCoachInfo
select teamName = (select TeamName from Team where po.TeamID = TeamID), c.experience, c.style, po.[group], po.hoursPerWeek
from Coach c join PartOf po on c.ID = po.PersonID
where @CID = c.ID

return
end