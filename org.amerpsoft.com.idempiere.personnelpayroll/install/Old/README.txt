			Installation Procedure

Plugin an Application Directory Steps:
	1. Pack in ‘AMERPSOFT Personnel Payroll T1’
	    Tables References 
	2. Pack in ‘AMERPSOFT Personnel Payroll T2’
		More Tables References 
	3. Pack in ‘AMERPSOFT Personnel Payroll T3’
		Windows
	4. Pack in ‘AMERPSOFT Personnel Payroll T4’
		Processes
	5. Pack in ‘AMERPSOFT Personnel Payroll T5’
		Menus
		Add Main Personnel Menu and Allocate all submenus imported
	6. Create Info Windows
		 Employee Info
		 Create it manually.
	7.  Create Amerp ToolBarButton Window.zip  (Toolbar Button App Window)
    	Toolbar Button App Window for Import ToolBar Buttons
    	Using AD_ToolbarButtons.xls File on org.amperpsoft.amfeatures
    	Add Amnually tool bars.
    	
	8. Packin ‘AMERP Personnel Payroll T6 Toolbar Buttons.zip’  
	    ** Delete before using delete_Toolbar_Buttons_Personnel.sql Script
	    This Avoid duplicate entries
	    Toolbar Buttons for Personnel Windows
		Some Windows Make IT Manually

	9. Add Zoom Condition to Table:
		Execute AD_Zoomcondition_Query.sql on Development Data Base
		
		Zoom Condition Name											Table Name		Table Description			Window Name
		Reconc ID PAYMENT											AMF_Payment_v	Payments VIEW		Payment
		Personnel and Payroll Preparing Social Benefit by RoleAcces	AMN_Contract	Payroll Contract	Personnel and Payroll Preparing Social Benefit by RoleAcces2
		Personnel and Payroll Preparing Payroll by RoleAccess		AMN_Contract	Payroll Contract	Personnel and Payroll Preparing Payroll by RoleAccess2
		Zoom to Employee ID											AMN_Employee	Payroll employee	Personnel and Payroll Employee
		AMN_Jobstation												AMN_Jobstation	Payroll Job Station	Personnel and Payroll Job Stations
		Personnel and Payroll Job Titles							AMN_Jobtitle	Payroll Job Title	Personnel and Payroll Job Titles
		Personnel and Payroll Job Unit								AMN_Jobunit		Payroll Job Unit	Personnel and Payroll Job Units
		Personnel and Payroll Preparing Social Benefit by RoleAcces	AMN_Location	Payroll Location	Personnel and Payroll Preparing Social Benefit by RoleAcces2
		Personnel and Payroll Preparing Payroll by RoleAccess		AMN_Location	Payroll Location	Personnel and Payroll Preparing Payroll by RoleAccess2
		Zoom to Period ID											AMN_Period		Payroll Period		Personnel and Payroll Periods for Payroll
		Personnel and Payroll Preparing Payroll by RoleAccess		AMN_Process		Payroll Process		Personnel and Payroll Preparing Payroll by RoleAccess2
		Personnel and Payroll Preparing Social Benefit by RoleAcces	AMN_Process		Payroll Process		Personnel and Payroll Preparing Social Benefit by RoleAcces2

	10. Create Views and Functions
		Folders: sql/views sql/functions
		Delete Tables if exist:
		amn_role_access_contract_v.sql
		amn_role_access_pro_v.sql
		
Note:
To Install Export Packages on a Development Database
1. Create a generic Pack-in record
2. Attach Export Package file
3. Impor the Pack
4. On Pack-out Window you will have the Records
		