use [Swimming Information Database]
go

create function dbo.getTimeOffCut(
@PID int, 
@Distance int,
@Stroke varchar(20),
@Unit varchar(5),
@Level varchar(30)
)
returns @getTimeOffCut table(
Distance int,
Stroke varchar(20), 
Unit varchar(5),
Level varchar(30),
[Time] time,
CutTime time,
TimeOff int
)
as
begin
declare @EID int
set @EID = (select EventID from [Event] where @Distance = Distance AND @Unit = Unit AND @Stroke = Stroke)

declare @currentTime time
set @currentTime = (select time from CompetesIn where @PID = SwimmerID AND @EID = EventID)

declare @sex Nvarchar(1)
set @sex = (select sex from person where @PID = ID)

declare @cutTime time
if @sex = 'M'
begin
set @cutTime = (select MaleTime from TimeStandard where @EID = EventID AND @Level = [Level])
end
else
begin
set @cutTime = (select FemaleTime from TimeStandard where @EID = EventID AND @Level = [Level])
end

insert @getTimeOffCut 
select @Distance, @Stroke, @Unit, @Level, @currentTime, @cutTime, datediff(second, @currentTime, @cutTime)

return
end
