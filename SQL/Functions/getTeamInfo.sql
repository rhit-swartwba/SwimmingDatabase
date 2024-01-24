use [Swimming Information Database]
go

create function getTeamInfo(
@PID int)

returns table
as
return(
select  t.TeamName, po.[Group], po.HoursPerWeek from PartOf po
join Team t on t.TeamID = po.TeamID
where @PID = po.PersonID
)
go

