/* Inserts the given equipment information to the equipment table

Example Usage:

DECLARE @Status SMALLINT
EXEC @Status =  [insert_equipment]
@model_1 = 'GX-Sonic-V',
@Brand_2 = 'Mizuno',
@Type_3 = 'Suit'
SELECT Status = @Status

Blaise Swartwood
*/

Use [Swimming Information Database]
Go

ALTER PROCEDURE [insert_equipment] (
@model_1 varchar(50),
@Brand_2 varchar(20),
@Type_3 varchar(20)
)

AS
SET NOCOUNT ON

IF @model_1 IS NULL OR @model_1 = '' OR @brand_2 IS NULL OR @brand_2 = '' 
OR @Type_3 IS NULL OR @Type_3 = ''
BEGIN
	RAISERROR('None of the given fields can be null or empty.', 14, 1)
	RETURN(1)
END

IF  (SELECT Count(@model_1)
	FROM [Equipment]
	WHERE (model = @model_1)) > 0
BEGIN
	RAISERROR('The given equipment already exists in the database.', 14, 2)
	RETURN(2)
END

-- inserting into the table now with the appropriate details
INSERT INTO [Equipment] 
( [Model], [Brand], [Type])
VALUES ( @Model_1, @Brand_2, @Type_3)

-- Check or any errors
DECLARE @Status SMALLINT
SET @Status = @@ERROR
IF @Status <> 0 
BEGIN	
	RAISERROR('An error occured in completing this operation', 14, @status)
	RETURN @status
END
ELSE
BEGIN	
	RETURN(0)
END