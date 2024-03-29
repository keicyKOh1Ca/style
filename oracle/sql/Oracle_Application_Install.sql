-- ◆◆◆共通データリンク作成◆◆◆
-- 事前にAPコンテナと配下のPDBを作成
-- APコンテナ内で構築するため、APコンテナに切り替え or 接続
-- begin the install of samp_app_01
alter pluggable database application SAMP_APP_01 begin install '1.0';

-- create the tablespace for the app
create tablespace TBSSAMPLE01 datafile size 10m autoextend on next 10m maxsize 200m;

-- create the user account usermaster01, which will own the app
create user USERMASTER01 identified by *********** container=all;

-- grant necessary privileges to this user account
grant create session, dba to USERMASTER01;

-- makes the tablespace that you just created the default for usermaster01
alter user USERMASTER01 default tablespace TBSSAMPLE01;

-- now connect as the application owner
-- sysのまま実行するので不要
--connect usermaster01/***********@【sid】

-- create a data-linked table
create table USERMASTER01.EMPLOYEE sharing=data
(
	EMP_ID			number(4),
	EMP_NAME		varchar2(60),
	HIRE_DATE		number(8),
	POSITION		number(2)
) tablespace TBSSAMPLE01;

-- insert records into employee
insert into USERMASTER01.EMPLOYEE values(1001, 'jone', 20000101, 1);
insert into USERMASTER01.EMPLOYEE values(1002, 'hide', 20000104, 1);
insert into USERMASTER01.EMPLOYEE values(1003, 'chad', 20000106, 2);
insert into USERMASTER01.EMPLOYEE values(1004, 'jenny', 20000121, 4);
commit;

-- end the application installation
alter pluggable database application SAMP_APP_01 end install '1.0';

-- ◆◆◆PDBに切り替え or 接続◆◆◆
connect PDBUSER01/***********@【pdbsid】
or
alter session set container = 【pdb】

-- application同期
alter pluggable database application SAMP_APP_01 sync;
