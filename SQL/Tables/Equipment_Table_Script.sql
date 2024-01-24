USE [Swimming Information Database]
GO

/****** Object:  Table [dbo].[Equipment]    Script Date: 4/5/2023 12:40:20 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Equipment](
	[Model] [varchar](50) NOT NULL,
	[Brand] [varchar](20) NULL,
	[Type] [varchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[Model] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

