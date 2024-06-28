&lArr;[COMMUNITY PLUGINS](../README.md) | [Home](../README.md)
<br />
<div align="center">
  <a href="AMERPSOFT_logo">
    <img src="../images/AMERPSOFT_logo_600.png" alt="Logo" width="150" height="90">
  </a>
</div>

<a name="readme-top"></a>

# AMERPSOFT Personnel and Payroll Plugin

## <b>Description</b>


AMERPSOFT Personnel and Payroll Plugin is related to Idempiere processes related with Employees. This plugin includes some new tasks, processes and reports.

New set of tables with AMN_ prefix are create to acomplish this tasks.

<b>Content:</b>

```text
- Initial installation procedures
- Installing plugin
- Processes
- Payroll Reports
```
Follow steps:

| Steps | Title                                          | Comments                                                                         |
| ----: | ---------------------------------------------- | -------------------------------------------------------------------------------- |
|     1 | [Initial installation procedures](#step1)      | Initial installation procedures                              				    |
|     2 | [Installing plugin](#step2)                 	 | Installing plugin          														|
|     3 | [Configuring basic tables](#step3)             | Configuring basic tables                                                			|
|     4 | [Processes](#step4)                            | Verify processes installed                                                         |
|     5 | [Personnel and Payroll Reports](#step5)                   | Review Personnel and Payroll reports Reports                                                          |
                                                 |


## <a name="step1"></a>⭐️1.	Initial installation procedures.

```text

```


<p align="left">(<a href="#readme-top">back to top</a>)</p>

## <a name="step2"></a>⭐️2. Installing plugin

Before installing ....


<p align="left">(<a href="#readme-top">back to top</a>)</p>

## <a name="step3"></a>⭐️3. Install Personnel and Payroll plugin 

<b>Install Plugin using Apache felix Web Console</b>

```text
	- Download plugin jar file from Repository.
    (Named as: org.amerpsoft.com.idempiere.personellpayroll_11.0.0.202404091015.jar )
	- Install using Osgi Apache Felix Web Console
	- or Any Manual procedure
	- Verify plugin is running and updated
```

<b>Pack-IN AMERPSFOT Personnel and Payroll # 1 to 6.zip</b>

```text
	 1. Download ‘AMERPSOFT Financial.zip’
	 2. Pack-IN using Application Dictionary --> Pack In menu 
	 3. Run Synchronize Terminology, Sequence Check and Role Access Update
	 4. Restart Server
 ```


<p align="left">(<a href="#readme-top">back to top</a>)</p>

## <a name="step4"></a>⭐️4. Personnel and Payroll Processes. 

AMERPSOFT Personnel and Payroll  Processes

```text
- Process 1
- Process 2
```
<p align="left">(<a href="#readme-top">back to top</a>)</p>

## <a name="step5"></a>⭐️5. Personnel and Payroll Reports 

AMERPSOFT Personnel and Payroll Reports

```text
- Report 1
- Report 2
```

These reports have been tested with Postgresql database. Oracle versions are coming soon.

PDF samples:

- [Account Elements](./install/pdf/CatalogoElementosCuenta.pdf)
- [State Financial Balance](./install/pdf/BalanceSituacionFinanciera.pdf)
- [State Financial Integral Results](./install/pdf/EstadoResultadosIntegrales.pdf)
- [Trial Balance](./install/pdf/BalanceComprobacionPeríodo.pdf)


<p align="left">(<a href="#readme-top">back to top</a>)</p>


## Requires Idempiere Release 11 

  Under Test - See release-11 branch for more details.

<p align="left">(<a href="#readme-top">back to top</a>)</p>

