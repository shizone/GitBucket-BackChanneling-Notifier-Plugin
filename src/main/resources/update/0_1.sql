-- Add back channeling
CREATE TABLE BACK_CHANNELING(
  USER_NAME          VARCHAR(100) NOT NULL,
  REPOSITORY_NAME    VARCHAR(100) NOT NULL,
  URL                VARCHAR(200) NOT NULL,
  AUTHORIZATION_CODE VARCHAR(16)  NOT NULL,
  THREAD_ID          BIGINT        NOT NULL
);
ALTER TABLE BACK_CHANNELING ADD PRIMARY KEY (USER_NAME, REPOSITORY_NAME);