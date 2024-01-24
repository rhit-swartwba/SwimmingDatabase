use [Swimming Information Database]
go

create view viewTimeStandards
as
select ts.[level], e.Distance, e.Stroke, e.Unit, ts.MaleTime, ts.FemaleTime
from TimeStandard ts
join [Event] e on e.EventID = ts.EventID



