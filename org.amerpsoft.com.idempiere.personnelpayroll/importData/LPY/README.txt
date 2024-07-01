Client Data Steps:
	1. Combinaciones de Cuentas Tabla AMN_Schema
		En principio se descontinua la aplicacion de esta tabla en la lógica de negocio.
		
	1. Import AMN Tables Data for:
		AMN_Rates			(Export_AMN_Rates.csv)		2024
		AMN_Process			(Export_AMN_Process.csv)
		AMN_Position 		(Export_AMN_Position.csv)	Taken from C_Job
		
	2. Manually Create Contracts
		AMN_Contract		(Export_AMN_Contract.csv) Un solo contrato generico MOLI
		
	3. Manually Create Locations
		AMN_Location
		
	4. Manually Create Departments
		AMN_Department
		
	5. Manually Create AMN_Schema
		AMN_Schema	(Default Accounts)
		
	6. Manually Create Job Structure and Shift
		Configure Activities Tree
		AMN_JobUnit
		AMN_JobStation
		AMN_JobTitles
		
	7. Manually create Shift
		AMN_Shift
		AMN_Shift_Detail
		
	8. Import Concepts Tables Data
		AMN_Concept_UOM		(Export_AMN_Concept_UOM.csv)
		AMN_Concept_Types	(Export_AMN_Concept_Types.csv)

	9. Manually Configure
		AMN_Concept_Types_Proc
		AMN_Concept_Types_Contract

	10. Manually Configure Accounting for Concepts
		AMN_Concept_Types_Acct
	
	11. Import  Employees as Business Partners
		Import Business Partners data for Employees
		Manually complete Employees data.
		 
	12. Status Lines
		Manually
		
	13. Manually Configure AMN_Rules
		See documentation for samples