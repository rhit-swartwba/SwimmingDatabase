/* Deletes the givens information from the Equipment Table

Example Usage:

DECLARE @Status SMALLINT
EXEC @Status =  [delete_equipment]
@model_1 = 'GX-Sonic-V'
SELECT Status = @Status

Blaise Swartwood
*/

Use [Swimming Information Database]
Go

ALTER PROCEDURE [delete_equipment] (
@model_1 varchar(50)
)

AS
SET NOCOUNT ON

IF @model_1 IS NULL OR @model_1 = ''
BEGIN
	RAISERROR('None of the given fields can be null', 14, 1)
	RETURN(1)
END

IF (SELECT Count(@model_1)
	FROM [Equipment]
	WHERE (model = @model_1)) = 0
BEGIN
	RAISERROR('The equipment given does not exist in the database.', 14, 2)
	RETURN(2)
END

-- deleting into the table now with the appropriate details
DELETE [Equipment] 
WHERE ( model = @model_1)

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