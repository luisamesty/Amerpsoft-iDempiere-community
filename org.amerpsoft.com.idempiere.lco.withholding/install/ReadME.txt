PAck-IN files as System:
1. AMERPSOFT LCO Withholding
	AMERPSOFT LCO Withholding.csv					(Pack-out CSV file)
	AMERPSOFT LCO Withholding.zip					(Pack-out Zip file)
	Export Package AMERPSOFT LCO Withholding.zip  	(Pack-out of Pack-out)
	
	Contains main LCO Tables, References Windows.
	META-INF: 2Pack_7.1.1.zip
	
2. AMERPSOFT LCO Withholding Windows
	AMERPSOFT LCO Withholding Windows.csv					(Pack-out CSV file)
	AMERPSOFT LCO Withholding Windows.zip					(Pack-out Zip file)
	Export Package AMERPSOFT LCO Withholding Windows.zip	(Pack-out of Pack-out)
	
	Contains new Windows for Withholding Documents on Customers and Vendors
	META-INF: 2Pack_7.1.2.zip
	
	Special NOTE for AMERPSOFT LCO Withholding Windows.zip:
	AD_TableIndex Keys duplicates must be deleted on PackOut.xml contained on zip file.
	Uncompless it -  Delete them - Compress it Again
	
PAck-IN files as Tenant user:
1. AMERPSOFT LCO Withholding DATA
	AMERPSOFT LCO Withholding DATA.csv
	AMERPSOFT LCO Withholding Data.zip


Client Install Steps.

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