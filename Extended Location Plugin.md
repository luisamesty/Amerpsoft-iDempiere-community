## AMERPSOFT Editor - Extended Location 
## Contents
1. [Repository Information]()
2. [Description]()
3. [Installation Idempiere version 6.2 and UP]()
4. [Installation Idempiere version 2.1 to 5.1]()

### Repository Information

-[ Mantainer: Luis Amesty](http://wiki.idempiere.org/en/User:Luisamesty)

-[ Copyright: Amerpsoft and contributors](http://amerpsoft.com/index.php/es/)

-[ License: Free as in Freedom not Free Lunch](http://www.gnu.org/licenses/gpl-2.0.html)

-[Souce Code: Bitbucket](https://bitbucket.org/amerpsoft/amerpsoft-idempiere-community/src/default/)

-[PDF Extended Demography](https://bitbucket.org/amerpsoft/amerpsoft-idempiere-community/downloads/DemografiaExtendida_amxeditor.pdf)


* '''Download:''' [https://bitbucket.org/amerpsoft/amerpsoft-editors-com/downloads/  amerpsoft-editors-com.zip ]
* '''PDF:''' [https://bitbucket.org/amerpsoft/amerpsoft-editors-com/src/default/documentation/DemografiaExtendida_amxeditor.pdf  PDF Manual]
* '''Venezuela:''' [https://bitbucket.org/amerpsoft/amerpsoft-editors-com/src/default/documentation/Venezuela%20ExtendedDemography.zip  Venezuela Demography]


== Description ==
This Plugin is related to Demographic Aspects on Idempiere ERP.  It is well known that on some countries there is a political division with concepts that go beyond country, region and city. 

For example in Spain we talk about Communities, they are related to Group of Provinces that keeps Local Government. They also have a political division for Provinces called Municipalities (Municipality-Municipio).

Another example is Venezuela, they have a political division for Provinces (Estados) called Municipalities (Municipality-Municipio) and a Municipality is also divided on Parishes (Parish-Parroquia). Particularity Main Mail office in Venezuela has published a new rule indicating the way postal address is to be written:

Communities, Region, Municipality and Parish, can be use for statistic and BI analysis on Sales, Cost, Personnel and other type of transaction results.

Postal Codes is another concern to be covered in future on this plugin, because a default value can be obtained from Region, Provinces, Municipality and Parish. Also Street Names provides a rule for finding zip codes.

In order to accomplish what have been explained before, some tables have to be modified, and some others tables have to be added to implement the new Window and plugin.

Added Tables: <br/>
C_Municipality, C_Parish, C_Zipcode,  C_Community 

Modified Tables <br/> 
(1) Add New Records  (2) Add New Fields <br/>
C_Country  (2) , AD_Message  (1),  C_Location  (2),  C_Region  (2)  , AD_Reference   (1) 
== Install Procedure ==
* '''Download:'''  DemografiaExtendida_amxeditor.pdf  
* '''From:'''  https://bitbucket.org/amerpsoft/amerpsoft-editors-com/downloads/  
*  '''See Appendix 1:''' Installation procedure

== Feedback ==
Please rate this plugin:

[[Category:Available_Plugins]]
[[Category:User Manual‏‎ ]]
[[Category:Business partners]]
