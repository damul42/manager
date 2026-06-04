# 권한 및 기능 관리

## 1. 기능 관리
각 기능은 메뉴로 표현되어 관리되며 메뉴들을 그룹으로 묶어주는 Module과 Module 밑의 Menu로 구성된다. 하나의 Module이 기능이 될 수 있고 하위의 메뉴가 기능이 될 수도 있다. 각각의 기능은 대표할 수 있는 Name과 URL로 표현되는 Access Point를 가지며 화면 표시용으로 icon을 가질 수 있으며 각각의 Status 값으로 Enable/Disable을 조절할 수 있다.
DB Scheme은 아래와 같다.

CREATE TABLE erp.tbl_sys_module (
	id varchar(45) NOT NULL,
	"name" varchar(100) NOT NULL,
	remark varchar(255) NULL,
	status varchar(5) NULL,
	seq int4 NULL,
	icon varchar(100) NULL,
	url varchar(255) NULL,
	regid varchar(45) NULL,
	regdate varchar(14) NULL,
	uptid varchar(45) NULL,
	uptdate varchar(14) NULL,
	CONSTRAINT tbl_sys_module_pkey PRIMARY KEY (id)
);


CREATE TABLE erp.tbl_sys_module_menu (
	id varchar(45) NOT NULL,
	moduleid varchar(45) NOT NULL,
	seq int4 NULL,
	"name" varchar(100) NULL,
	icon varchar(100) NULL,
	status varchar(5) NULL,
	url varchar(255) NULL,
	original varchar(100) NULL,
	CONSTRAINT tbl_sys_module_menu_pkey PRIMARY KEY (id)
);
CREATE INDEX tbl_sys_module_menu_moduleid_idx ON erp.tbl_sys_module_menu USING btree (moduleid);


## 2. 권한 관리
권한은 각 메뉴(기능)에 대한 접근 권한의 조합으로 표시되며 Role과 각 기능의 접근권한의 조합으로 이루어진 Role Setting의 조합으로 구성된다. 
기능에 대한 접근 권한은 각각 Admin, Write, Read, List로 구분될 수 있으며 Write는 쓰기 권한, Read는 읽기 권한, List는 목록 보기 권한으로 표현된다. 특별히 추가적인 권한을 부여할 필요가 있는 경우 Admin 권한을 활용할 수 있다. 예를들어 모든 조직의 데이터를 컨트롤할 필요가 있을 경우 Admin 권한을 주고 Write 권한의 자신의 조직 데이터로 한정할 수 있다.
DB Scheme은 아래와 같다.

CREATE TABLE erp.tbl_sys_role (
	id varchar(45) NOT NULL,
	"name" varchar(100) NOT NULL,
	remark varchar(255) NULL,
	regid varchar(45) NULL,
	regdate varchar(14) NULL,
	uptid varchar(45) NULL,
	uptdate varchar(14) NULL,
	CONSTRAINT tbl_sys_role_pkey PRIMARY KEY (id)
);


CREATE TABLE erp.tbl_sys_role_setting (
	id varchar(45) NOT NULL,
	roleid varchar(45) NOT NULL,
	moduleid varchar(45) NOT NULL,
	menuid varchar(45) NULL,
	adminp varchar(5) NOT NULL,
	listp varchar(5) NOT NULL,
	readp varchar(5) NOT NULL,
	writep varchar(5) NOT NULL,
	pindex int4 NOT NULL,
	CONSTRAINT tbl_sys_role_setting_pkey PRIMARY KEY (id)
);
CREATE INDEX tbl_sys_role_setting_roleid_idx ON erp.tbl_sys_role_setting USING btree (roleid);

## 3. 권한 할당
작성된 권한은 사용자에게 할당될 수 있으며 복수 개의 Role ID가 사용자에게 할당되고 사용자에게 할당된 접근 권한은 할당된 Role Setting의 Union으로 높은 권한의 Setting을 택한다.

## 4. 권한 적용
