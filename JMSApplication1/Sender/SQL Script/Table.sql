CREATE TABLE Messages(
   id_message NUMBER(20) PRIMARY KEY,
   destination VARCHAR(5) NOT NULL,
   text VARCHAR2(1000),
   date_recording TIMESTAMP NOT NULL,
   replay VARCHAR2(5) NOT NULL,
   replay_text VARCHAR2(1000),
   errors CLOB
);

TRUNCATE TABLE Messages;