-- ◆◆◆アプリケーション・アンインストール◆◆◆
ALTER PLUGGABLE DATABASE APPLICATION SAMP_APP BEGIN UNINSTALL;

-- APに紐付いているオブジェクトの削除が必要
ALTER PLUGGABLE DATABASE APPLICATION SAMP_APP END UNINSTALL
*
行1でエラーが発生しました。:
ORA-65344: オブジェクト、ユーザー、ロールおよびプロファイルのあるアプリケーションは、アンインストールまたはパージできません

-- drop table
DROP TABLE SAMP_USER.DEPT CASCADE CONSTRAINTS PURGE;

-- drop user
drop user SAMP_USER CASCADE;

ALTER PLUGGABLE DATABASE APPLICATION SAMP_APP END UNINSTALL;

-- この状態でまだアプリが存在する。
select app_name from dba_applications;

APP_NAME
-------------
SAMP_APP

-- 一旦disconして再connしても、残る
select app_name from dba_applications;

APP_NAME
-------------
SAMP_APP

-- アンインストールという状態で残る。
seelct APP_NAME,APP_STATUS from dba_applications;

APP_NAME		APP_STATUS	
----------------------------
SAMP_APP		UNINSTALLED	


--◆同一名でAPLを作成しようとすると怒られる。
-- Begin the install of SAMP_APP
ALTER PLUGGABLE DATABASE APPLICATION SAMP_APP BEGIN INSTALL '1.0';
*
行1でエラーが発生しました。:
ORA-65245: アプリケーションSAMP_APPはアンインストールされました
