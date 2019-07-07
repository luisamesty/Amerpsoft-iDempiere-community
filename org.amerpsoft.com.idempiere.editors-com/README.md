
AMERP Amerpsoft-editors: 
=======================
5.1 Actualizado Marzo 2018
Created and Starting Branch release-5.1 on March 13 2019
default on idempiere release 6.2
*********************************************************
Description
-----------
This Plugin is related to Demographic Aspects on Idempiere ERP. It is well known that on some countries there is a political division with concepts that go beyond country, region and city.

For example in Spain we talk about Communities, they are related to Group of Provinces that keeps Local Government. They also have a political division for Provinces called Municipalities (Municipality-Municipio).

Another example is Venezuela, they have a political division for Provinces (Estados) called Municipalities (Municipality-Municipio) and a Municipality is also divided on Parishes (Parish-Parroquia). Particularity Main Mail office in Venezuela has published a new rule indicating the way postal address is to be written:

Communities, Region, Municipality and Parish, can be use for statistic and BI analysis on Sales, Cost, Personnel and other type of transaction results.

Postal Codes is another concern to be covered in future on this plugin, because a default value can be obtained from Region, Provinces, Municipality and Parish. Also Street Names provides a rule for finding zip codes.

In order to accomplish what have been explained before, some tables have to be modified, and some others tables have to be added to implement the new Window and plugin.

Added Tables: 
CMunicipality, CParish, CZipcode, CCommunity

Modified Tables 
(1) Add New Records (2) Add New Fields 
CCountry (2) , ADMessage (1), CLocation (2), CRegion (2) , AD_Reference (1)

* Amerpsoft Editors includes Extended Demography
* Update for idempiere version 5.1
* Revisada 3 Septiembre 2018

Features
--------
- Copyright: 
    2018 Luis Amesty
- License: GPLv2
- Language: Java
- IDE: Eclipse

Documentation
-------------
- Wiki Idempiere [Extended Location](http://wiki.idempiere.org/en/Plugin:_Extended_Location)
- Download PDF 'DemografiaExtendida_amxeditor.pdf'

Installation
-------------
- Download PDF 'DemografiaExtendida_amxeditor.pdf'
- See Apendix A1 , Installation Procedure.

Questions or feedback
---------------------
- Feel free to ask and comment in the [group](https://groups.google.com/forum/#!forum/idempiere)