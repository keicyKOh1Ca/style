
-- ここで指定しているDIRは必ず、Create文を実行する前に、作成しておくこと！
--
-- !mkdir /u01/app/oracle/product/oradata/CDB01/onlinelog
-- !mkdir /u01/app/oracle/product/oradata/CDB01/datafile
-- !mkdir /u01/app/oracle/product/oradata/CDB01/pdbseed

CREATE DATABASE CDB01
    USER SYS IDENTIFIED BY "レスポンスファイルで指定したsysパスワード"
    USER SYSTEM IDENTIFIED BY "レスポンスファイルで指定したsystemパスワード"
    LOGFILE   GROUP 1 (' /u01/app/oracle/product/oradata/CDB01/onlinelog/redo01a.log', ' /u01/app/oracle/product/oradata/CDB01/onlinelog/redo01b.log') SIZE 100M BLOCKSIZE 512,
            GROUP 2 (' /u01/app/oracle/product/oradata/CDB01/onlinelog/redo02a.log', ' /u01/app/oracle/product/oradata/CDB01/onlinelog/redo02b.log') SIZE 100M BLOCKSIZE 512,
            GROUP 3 (' /u01/app/oracle/product/oradata/CDB01/onlinelog/redo03a.log', ' /u01/app/oracle/product/oradata/CDB01/onlinelog/redo03b.log') SIZE 100M BLOCKSIZE 512,
            GROUP 4 (' /u01/app/oracle/product/oradata/CDB01/onlinelog/redo04a.log', ' /u01/app/oracle/product/oradata/CDB01/onlinelog/redo04b.log') SIZE 100M BLOCKSIZE 512,
            GROUP 5 (' /u01/app/oracle/product/oradata/CDB01/onlinelog/redo05a.log', ' /u01/app/oracle/product/oradata/CDB01/onlinelog/redo05b.log') SIZE 100M BLOCKSIZE 512
    MAXLOGHISTORY 1
    MAXLOGFILES 32
    MAXLOGMEMBERS 5
    MAXDATAFILES 1024
    CHARACTER SET AL32UTF8
    NATIONAL CHARACTER SET AL16UTF16
    EXTENT MANAGEMENT LOCAL
    DATAFILE ' /u01/app/oracle/product/oradata/CDB01/datafile/system01.dbf'
        SIZE 10G REUSE AUTOEXTEND ON NEXT 10240K MAXSIZE UNLIMITED
    SYSAUX DATAFILE ' /u01/app/oracle/product/oradata/CDB01/datafile/sysaux01.dbf'
        SIZE 5G REUSE AUTOEXTEND ON NEXT 10240K MAXSIZE UNLIMITED
    DEFAULT TABLESPACE users
        DATAFILE ' /u01/app/oracle/product/oradata/CDB01/datafile/users01.dbf'
        SIZE 100M REUSE AUTOEXTEND ON NEXT 10240K MAXSIZE UNLIMITED
    DEFAULT TEMPORARY TABLESPACE tempts1
        TEMPFILE ' /u01/app/oracle/product/oradata/CDB01/datafile/temp01.dbf'
        SIZE 1G REUSE AUTOEXTEND ON NEXT 10240K MAXSIZE 10G
    UNDO TABLESPACE undotbs1
        DATAFILE ' /u01/app/oracle/product/oradata/CDB01/datafile/undotbs01.dbf'
        SIZE 1G REUSE AUTOEXTEND ON NEXT 5120K MAXSIZE 10G
    ENABLE PLUGGABLE DATABASE
    SEED FILE_NAME_CONVERT=(' /u01/app/oracle/product/oradata/CDB01/datafile', '/u01/app/oracle/product/oradata/CDB01/pdbseed')
        SYSTEM DATAFILES SIZE 5G AUTOEXTEND ON NEXT 100M MAXSIZE UNLIMITED
        SYSAUX DATAFILES SIZE 2G AUTOEXTEND ON NEXT 100M MAXSIZE UNLIMITED
    LOCAL UNDO ON
    SET TIME_ZONE='Asia/Tokyo'
    ;
