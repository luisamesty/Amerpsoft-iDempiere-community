-- UPDATES
-- UPDATE ZK_THEME with ksys
UPDATE adempiere.ad_sysconfig
SET ad_client_id=0, ad_org_id=0, created='2012-11-02 14:39:46.000', updated='2024-04-13 15:42:05.635', createdby=100, updatedby=10, isactive='Y', "name"='ZK_THEME', value='ksys', description='Available choices amtheme amthemeblu businessteam businessTeamBlue ksys kbs iceblue optumTheme iceblue_c', entitytype='D', configurationlevel='S', ad_sysconfig_uu='054399ad-3705-411d-b79e-7ec7111888c5'
WHERE ad_sysconfig_id=200021;
-- UPDATE ZK_THEME_USE_FONT_ICON_FOR_IMAGE to N
UPDATE adempiere.ad_sysconfig
SET ad_client_id=0, ad_org_id=0, created='2019-11-01 09:37:20.427', updated='2024-04-13 15:49:28.092', createdby=0, updatedby=10, isactive='Y', "name"='ZK_THEME_USE_FONT_ICON_FOR_IMAGE', value='N', description='Use Font Icon Yes or No', entitytype='D', configurationlevel='S', ad_sysconfig_uu='29a0d95d-af05-477a-a047-241c350dc8e3'
WHERE ad_sysconfig_id=200111;

--INSERTS
INSERT INTO adempiere.ad_sysconfig
(ad_sysconfig_id, ad_client_id, ad_org_id, created, updated, createdby, updatedby, isactive, "name", value, description, entitytype, configurationlevel, ad_sysconfig_uu)
VALUES(1000000, 0, 0, '2018-11-13 09:48:12.738', '2024-04-13 15:43:49.014', 0, 10, 'Y', 'ZK_LOGO_LARGE', '~./theme/ksys/images/login-logo-amerp2.png', 'Login Logo Large', 'AMERP', 'S', 'bfe6284e-00a2-412f-be82-37ed38363768');
INSERT INTO adempiere.ad_sysconfig
(ad_sysconfig_id, ad_client_id, ad_org_id, created, updated, createdby, updatedby, isactive, "name", value, description, entitytype, configurationlevel, ad_sysconfig_uu)
VALUES(1000001, 0, 0, '2018-11-13 09:48:57.580', '2024-04-13 15:44:27.074', 0, 10, 'Y', 'ZK_LOGO_SMALL', '~./theme/ksys/images/header-logo-amerp.png', 'Login Logo Small', 'AMERP', 'S', '69007345-c36f-4b02-870a-c41d8fb9deea');
INSERT INTO adempiere.ad_sysconfig
(ad_sysconfig_id, ad_client_id, ad_org_id, created, updated, createdby, updatedby, isactive, "name", value, description, entitytype, configurationlevel, ad_sysconfig_uu)
VALUES(1000012, 0, 0, '2024-04-13 15:45:35.250', '2024-04-13 15:46:13.602', 10, 10, 'Y', 'ZK_BROWSER_TITLE', 'iDempiere AMERP', 'Browser Title', 'AMERP', 'S', 'c587f6f8-2910-4b27-913e-2e41e8581ea0');
INSERT INTO adempiere.ad_sysconfig
(ad_sysconfig_id, ad_client_id, ad_org_id, created, updated, createdby, updatedby, isactive, "name", value, description, entitytype, configurationlevel, ad_sysconfig_uu)
VALUES(1000013, 0, 0, '2024-04-13 15:47:04.492', '2024-04-13 15:48:12.320', 10, 10, 'Y', 'ZK_BROWSER_ICON', '~./theme/ksys/images/icon-idempiere.png', 'Browse Icon Path', 'AMERP', 'S', '4cf766fd-5982-49dd-82f2-e8347214dbf0');
