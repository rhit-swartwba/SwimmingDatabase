/* Updates the given equipment information from the Equipment Table

Example Usage:

DECLARE @Status SMALLINT
EXEC @Status =  [update_equipment]
@model_1 = GX-Sonic-V
@Brand_2 Mizuno
@Type_3 Suit
SELECT Status = @Status

Blaise Swartwood
*/

Use [Swimming Information Database]
Go

ALTER PROCEDURE [update_equipment] (
@model_1 varchar(50),
@Brand_2 varchar(20),
@Type_3 varchar(20)
)

AS
SET NOCOUNT ON

IF @model_1 IS NULL OR @model_1 = '' OR @brand_2 IS NULL OR @brand_2 = '' 
OR @Type_3 IS NULL OR @Type_3 = ''
BEGIN
	RAISERROR('None of the fields can be null or empty', 14, 1)
	RETURN(1)
END

IF  (SELECT Count(@model_1)
	FROM [Equipment]
	WHERE (model = @model_1)) = 0

BEGIN
	RAISERROR('The given equipment value does not exist in the database.', 14, 2)
	RETURN(2)
END

-- inserting into the table now with the appropriate details
Update [Equipment] 
SET brand = @Brand_2, type = @Type_3
WHERE model = @model_1

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