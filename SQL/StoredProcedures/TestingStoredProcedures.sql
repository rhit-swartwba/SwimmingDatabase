--TESTING 

--EQUIPMENT
Use [Swimming Information Database]
Go

DECLARE @Status SMALLINT
EXEC @Status =  [insert_equipment]
@model_1 = 'Vanquishers',
@Brand_2 = 'Speedo',
@Type_3 = 'Goggle'
SELECT Status = @Status

DECLARE @Status SMALLINT
EXEC @Status =  [update_equipment]
@model_1 = 'Vanquishers',
@Brand_2 = 'Speedo',
@Type_3 = 'Goggle'
SELECT Status = @Status

DECLARE @Status SMALLINT
EXEC @Status =  [delete_equipment]
@model_1 = 'bob'
SELECT Status = @Status

--TEAM

DECLARE @Status SMALLINT
EXEC @Status =  [insert_team]
@TeamName_input = 'Tst',
@Region_input = ''
SELECT Status = @Status


DECLARE @Status SMALLINT
EXEC @Status =  [update_team]
@TeamName_input = 'KSC',
@Region_input = 'West'
SELECT Status = @Status


DECLARE @Status SMALLINT
EXEC @Status =  [delete_team]
@TeamName_input = 'Test'
SELECT Status = @Status

-- Event

DECLARE @Status SMALLINT
EXEC @Status =  [insert_event]
@Distance_1 = 200,
@Stroke_2 = 'Butterfly',
@Unit_3 = 'SCY'
SELECT Status = @Status

-- PartOf

DECLARE @Status SMALLINT
EXEC @Status =  [insert_partof]
@PersonFName_1 = 'Brian',
@PersonLName_2 = 'Beasley',
@TeamName_3 = 'Tide',
@Group_4 = 'JV',
@HoursPerWeek_5 = 14
SELECT Status = @Status

DECLARE @Status SMALLINT
EXEC @Status =  [update_partof]
@PersonFName_1 = 'Brian',
@PersonLName_2 = 'Beasley',
@TeamName_3 = 'Tide',
@Group_4 = 'Nationals',
@HoursPerWeek_5 = 6
SELECT Status = @Status

DECLARE @Status SMALLINT
EXEC @Status =  [delete_partof]
@PersonFName_1 = 'Brian',
@PersonLName_2 = 'Beasley',
@TeamName_3 = 'Tide'
SELECT Status = @Status