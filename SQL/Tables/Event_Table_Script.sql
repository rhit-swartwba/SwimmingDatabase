USE [Swimming Information Database]
GO

/****** Object:  Table [dbo].[Event]    Script Date: 4/5/2023 12:41:05 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Event](
	[EventID] [int] IDENTITY(1,1) NOT NULL,
	[Distance] [int] NOT NULL,
	[Stroke] [varchar](40) NOT NULL,
	[Unit] [varchar](10) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[EventID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Event]  WITH CHECK ADD CHECK  (([Distance]>(0) AND [Distance]<=(1650)))
GO

ALTER TABLE [dbo].[Event]  WITH CHECK ADD CHECK  (([Stroke]='IM' OR [Stroke]='Freestyle' OR [Stroke]='Breaststroke' OR [Stroke]='Backstroke' OR [Stroke]='Butterfly'))
GO

ALTER TABLE [dbo].[Event]  WITH CHECK ADD CHECK  (([Unit]='SCM' OR [Unit]='SCY' OR [Unit]='LCM'))
GO

