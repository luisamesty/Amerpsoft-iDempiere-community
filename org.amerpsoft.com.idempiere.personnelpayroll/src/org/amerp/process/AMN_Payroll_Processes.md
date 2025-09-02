# **AMN Payroll Processes:**

## Payroll Period Generation.

Period Generation is the process that creates Payroll Periods on
AMN_Period table, taking into account Fiscal Year, Calendar and Contract
periods. This Process can be executed from "***Personnel and Payroll
Preparing by Roles***" Window.

**[AMNYearCreatePeriods]{.underline}**

Description: Procedure called from iDempiere AD

Parameters:

C_Year_ID

C_Calendar_ID

AMN_Process_ID

AMN_Contract_ID

Result: AMN Personnel and Payroll Create Periods of year on

AMN_Period Table

## Payroll Document Generation.

Different processes involved on Payroll Document Generation. All of the
are called from "***Personnel and Payroll Preparing by Roles***" Window.

**[AMNPayrollCreate]{.underline}**

**Description: Procedure called from iDempiere AD**

**Create Payroll Receipts**

**For One Process, One Contract and One Period**

**For All Employees on Contract and Period.**

**Result:Create all PayrollReceipt according to Payroll Concepts Rules**

> **Create Header on AMN_Payroll and Lines on AMN_Payroll_detail**

**Using AMNPayrollCreateDocs Class**

**[AMNPayrollCreateDocs]{.underline}**

**Description: Generic Class AMNPayrollCreateDocs**

**Called from: AMNPayrollCreate (Creates Receipts for Period)**

**AMNPayrollCreateOneDoc (Creates One Receipt)**

**Procedures:**

**CreatePayrollDocuments**

**CreatePayrollDocumentsLines**

**CreatePayrollDetailLines**

**CalculatePayrollDocuments**

**CreatePayrollOneDocument**

**CreatePayrollOneDocumentLines**

**CreatePayrollOneDocDetailLines**

**CalculateOnePayrollDocument**

**[AMNPayrollCreateOneDoc]{.underline}**

Description: Procedure called from iDempiere AD

Create Payroll Receipt

For One Process, One Contract, One Period and One Employee

Result: Create PayrollReceipt according to Payroll Concepts Rules

> Create Header on AMN_Payroll and Lines on AMN_Payroll_detail

Using AMNPayrollCreateDocs Class

## Attendance Period Generation.

This process is involved on Payroll Atendance Period Generation.
Personnel attendance records are executed on three steps:

-   Attendance Registration .

-   Attendance Processing.

-   Atthendance Transfering.

On Window called "***Personnel and Payroll Attendance Registry by
Periods***" , users can define Attendance Periods. In other words,
Attendance Days, related to Fiscal Year, Month and Payroll Period. Each
Day is called 'Attendance Period". They must be generated before
registering Attendance.

**[AMNPayrollCreatePeriodAssistPeriods]{.underline}**

Description: Procedure called from iDempiere AD

AMN Personnel and Payroll Create Periods of year

For One Fiscal Year and One Payroll Period

Result: Create records on AMN_Period_Assist Table

according to Fiscal Year and Payroll Period.

## Payroll Document Processing.

These processes are involved on Payroll Document Processing. Processing
means completing and accounting a Payroll Document. For Processing a
Document, it must be on 'Draft Mode (DR)', after processing it wil be on
'Completed Mode CO', and all accounting related with transaction is
performed.

**[AMNPayrollProcessOneDoc]{.underline}**

Description: Procedure called from iDempiere AD

Process and Accounts Payroll Receipt for One Employee

For One Process, One Contract and One Employee

For One Employee on Contract and Period.

Result: Update Payroll Receipt on AMN_Payroll record

From DR Draft Mode to CO Completed.

## Payroll Attendance Processing.

These processes are involved on Payroll Attendance Processing.
Attendance Processing means validation from Attendance Registered from
CSV Files downloaded and transformed from Biometric Attendance
Equipment.

**[AMNPayrollProcessPayrollAssistProcess]{.underline}**

Description: Generic Class AMNPayrollCreatePayrollAssistProc

Called from: AMNPayrollProcessPayrollAssistOneEmployee

AMNPayrollProcessPayrollAssistOnePeriod

AMNPayrollProcessPayrollAssistOneAttendanceDay

Procedures:

CreatePayrollDocumentsAssistProcforEmployeeOneDay

getTimestampShifdetailEventTime

calcAttendanceValuesofPayrollVars

secondsOfTime

**[AMNPayrollProcessPayrollAssistOneAttendanceDay]{.underline}**

Description: Procedure called from iDempiere AD

Process Payroll Attendance for One Day

For One Process, All Employees and Contracts

Result: Create Payroll Attendance on AMN_Payroll_Assist_Proc

records according to AMN_Payroll_Assist Inputs and

Output

**[AMNPayrollProcessPayrollAssistOneEmployee]{.underline}**

Description: Procedure called from iDempiere AD

Process Payroll Attendance for One Employee

For One Process, One Contract and One Employee

Result: Create Payroll Attendance on AMN_Payroll_Assist_Proc

> records according to AMN_Payroll_Assist Inputs and
>
> Output

**[AMNPayrollProcessPayrollAssistOnePeriod]{.underline}**

Description: Procedure called from iDempiere AD

Process Payroll Attendance for One Period

For One Process, One Contract and One Period

For All Employees on Contract and Period.

Result: Create Payroll Attendance on AMN_Payroll_Assist_Proc

> according to AMN_Payroll_Assist
>
> Inputs and Output

## Payroll Refresh.

These processes are involved on Payroll Calculation Procedures. Refresh
means recalculate Payroll Document from Concept Formulas ordered by
'*[CalcOrder']{.underline}* field.

**[AMNPayrollRefresh]{.underline}**

Description: Procedure called from iDempiere AD

Refresh or Recalculate One Payroll Receipt

For One Process, One Contract and One Period

One Employee.

Result: Recalculate One PayrollReceipt according to Payroll

Concepts Rules Update Header on AMN_Payroll and Lines on

AMN_Payroll_detail Using AmerpPayrollCalc Class

**[AMNPayrollRefreshOnePeriod]{.underline}**

Description: Procedure called from iDempiere AD

Refresh or Recalculate Payroll Receipts

For One Process, One Contract and One Period

For All Employees on Contract and Period.

Result: Recalculate all PayrollReceipt according to Payroll

Concepts Rules

> Update Header on AMN_Payroll and Lines on
>
> AMN_Payroll_detail Using AmerpPayrollCalc Class

## Payroll Attendance Transfer.

These processes are involved on Payroll Calculation Procedures. Transfer
means writing Normal Hours, Extra Hours , Attendance Concept and
Attendance Bonus Concept.

**[AMNPayrollTransferPayrollAssist]{.underline}**

Description: Generic Class AMNPayrollTransferPayrollAssist

Called from: AMNPayrollTransferPayrollAssistOneEmployee

> AMNPayrollTransferPayrollAssistOnePeriod

**[AMNPayrollTransferPayrollAssistOneEmployee]{.underline}**

Description: Procedure called from iDempiere AD

Transfer Payroll Attendance Processed and Generated for One

Employee, One Process, One Contract and One Employee

Result: Update Payroll Receipt on AMN_Payroll_Detail records

according to AMN_Payroll_Assist_Proc Previously

> Processed.

**[AMNPayrollTransferPayrollAssistOnePeriod]{.underline}**

Description: Procedure called from iDempiere AD

Transfer Payroll Attendance Processed and Generated for

> One Period, One Process, One Contract and One Period

For All Employees on Contract and Period.

Result: Update Payroll Receipts on AMN_Payroll_Detail records

according to AMN_Payroll_Assist_Proc Previously

> Processed.
