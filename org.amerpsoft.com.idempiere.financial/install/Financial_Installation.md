Amerpsoft-Financial Basic 
=========================
Installation Procedure: 
======================
System Install
==============
	1. Pack in ‘AMERP Financial.zip’
		Tables, References, Processes, Windows, Menus, 
	2. Create Views and Functions
		Folders: 
			sql/views 
			sql/functions
	3. Import LCO_ISIC.csv 

Client Install Steps
====================
	1. LCO ISIC
		EXPORT CSV and Import on New Client
	2. Tax Payer Type
		Create manually
	3. Tax Categories
		Export CSV and Import on New Client
	4. Tax Rates
		Export CSV and Import on New Client
		Adjust Accounting by Group, manuallly and using DB UPDATE Command
	5. Withholding Categories (Categorias de retención)
		EXPORT CSV and Import on New Client
	6. Withholding Types (Tipos de Retención)
		EXPORT CSV and Import on New Client
	7. Withholding Rules (Reglas de Retención)
		Rule Conf: Adjust manually
		Firts Create Withholding Calc and then Withholding Rules
		Withholding Calc: Export one by one and import 
		Withholding Rule: Export one by one and import 
