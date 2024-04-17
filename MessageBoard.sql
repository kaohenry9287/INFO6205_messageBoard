/*PART1 (create MessageBoard database)*/
CREATE DATABASE `MessageBoard` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;


/*PART2 (create User table)*/
CREATE TABLE `User` (
  `userId` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(60) NOT NULL,
  `loginDte` datetime,
  `logoutDate` datetime,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `userId_UNIQUE` (`userId`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


/*PART3 (create Board table)*/
CREATE TABLE `Board` (
  `boardId` varchar(45) NOT NULL,
  `boardName` varchar(45) NOT NULL,
  PRIMARY KEY (`boardId`),
  UNIQUE KEY `boardId_UNIQUE` (`boardId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


/*PART4 (create Article table)*/
CREATE TABLE `Article` (
  `articleID` varchar(45) NOT NULL,
  `boardID` varchar(45) NOT NULL,
  `authorID` varchar(45) NOT NULL,
  `title` text NOT NULL,
  `content` text NOT NULL,
  `createDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `commentCount` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`articleID`),
  UNIQUE KEY `articleID_UNIQUE` (`articleID`),
  KEY `boardID_idx` (`boardID`),
  KEY `authorID_idx` (`authorID`),
  CONSTRAINT `authorID` FOREIGN KEY (`authorID`) REFERENCES `User` (`userId`),
  CONSTRAINT `boardID` FOREIGN KEY (`boardID`) REFERENCES `Board` (`boardId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


/*PART5 (create Comment table)*/
CREATE TABLE `Comment` (
  `commentID` varchar(45) NOT NULL,
  `articleID` varchar(45) NOT NULL,
  `authorID` varchar(45) NOT NULL,
  `content` text NOT NULL,
  `createDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`commentID`),
  UNIQUE KEY `commentID_UNIQUE` (`commentID`),
  KEY `articleID_idx` (`articleID`),
  KEY `authorID_idx` (`authorID`),
  CONSTRAINT `articleID_comment_article` FOREIGN KEY (`articleID`) REFERENCES `Article` (`articleID`),
  CONSTRAINT `authorID_comment_user` FOREIGN KEY (`authorID`) REFERENCES `User` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE DEFINER=`root`@`localhost` TRIGGER `Comment_AFTER_INSERT` AFTER INSERT ON `comment` FOR EACH ROW BEGIN
UPDATE Article
    SET commentCount = commentCount + 1
    WHERE articleID = NEW.articleID;

END;

CREATE DEFINER=`root`@`localhost` TRIGGER `Comment_AFTER_DELETE` AFTER DELETE ON `comment` FOR EACH ROW BEGIN
UPDATE Article
    SET commentCount = commentCount - 1
    WHERE articleID = OLD.articleID;
END;
