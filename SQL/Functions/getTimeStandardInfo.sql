USE [Swimming Information Database]
GO


create function dbo.getTimeStandardInfo(
@Distance int,
@Stroke varchar(20),
@Unit varchar(5),
@Level varchar(30) )

returns @getTimeStandardInfo table(
MaleTime time,
FemaleTime time)
as
begin

declare @EID int
set @EID = (select EventID from [Event] 
					where @Distance = Distance AND @Stroke = Stroke AND @Unit = Unit)

insert @getTimeStandardInfo
select MaleTime, FemaleTime from TimeStandard 
where EventID = @EID
AND [Level] = @Level
return
end
