CREATE TABLE IF NOT EXISTS "Users" (
    "UserID" UUID PRIMARY KEY,
    "Username" VARCHAR(50) NOT NULL,
    "Password" TEXT NOT NULL,
    "Email" VARCHAR(255) NOT NULL UNIQUE,
    "RegistrationDate" TIMESTAMP,
    "LastLogin" TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "Video" (
    "VideoID" UUID PRIMARY KEY,
    "UserID" UUID,
    "CreationDate" TIMESTAMP,
    "Subject" TEXT,
    "AgeGroup" INTEGER,
    "MainCharacter" TEXT,
    "VideoURL" TEXT,
    "Language" TEXT
);

CREATE TABLE IF NOT EXISTS "GenerationLog" (
    "LogID" UUID PRIMARY KEY,
    "UserID" UUID,
    "VideoID" UUID,
    "Timestamp" TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "GenerationParameters" (
    "ParameterID" UUID PRIMARY KEY,
    "VideoID" UUID,
    "Subject" TEXT,
    "AgeGroup" TEXT,
    "MainCharacter" TEXT,
    "Language" TEXT,
    "ExerciseID" UUID
);

CREATE TABLE IF NOT EXISTS "MathExercise" (
    "ExerciseID" UUID PRIMARY KEY,
    "VideoID" UUID,
    "Question" TEXT,
    "Answer" TEXT,
    "Feedback" TEXT
);

CREATE TABLE IF NOT EXISTS "PrivateFairyTale" (
    "PrivateFairyTaleID" UUID PRIMARY KEY,
    "UserID" UUID,
    "Title" TEXT,
    "Content" TEXT,
    "Language" TEXT,
    "AudioURL" TEXT
);

CREATE TABLE IF NOT EXISTS "StoryImage" (
    "ImageID" UUID PRIMARY KEY,
    "PrivateFairyTaleID" UUID,
    "PublicFairyTaleID" UUID,
    "ImageURL" TEXT
);

