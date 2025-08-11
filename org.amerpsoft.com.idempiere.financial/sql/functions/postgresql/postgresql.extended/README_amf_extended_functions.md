# AMF Extended Functions

Function for Financial Reports

Reduce script text time consuming on Report Queries.

## amf_org_tree

- amf_org_tree.sql. 

Query to create function in order to return Organizations.

- amf_org_tree_pruebas.sql.

Test organizations queries.

## amf_element_value_tree_basic

- amf_element_value_tree_basic.sql.
Query to build function that retrieves Element value tree in basic mode.


- amf_element_value_tree_extended.sql.
Query to build function that retrieves Element value tree in extended mode with parent accounts in nine (9) levels.

- amf_element_value_tree_pruebas.sql
Queryies to test account element functions.

## amf_balance_account_org

- amf_balance_account_org.sql
Query for returning  the balance for a given period of an account and an Organization.
If parameter (p_ad_org_id) is null, then it retirns all Organizations balanace.

- amf_balance_account_org_test.sql.
Query to test amf_balance_account_org.



## amf_balance_account_org_flex

-amf_balance_account_org_flex.sql.
Query to build function that retrieves balance for a given Period or Date Range for an account and an organization.

- amf_balance_account_org_flex_test.sql.
Query to test 

amf_balance_account_org_flex2.sql

## amf_balance_account_org_flex_orgparent

-amf_balance_account_org_flex_orgparent.sql.
Query to build function that retrieves balance for a given Period or Date Range for an account and an organization parent or organization (flex).

- amf_balance_account_org_flex_orgparent_test.sql.
Query to test 

## amf_balance_account_org_by_dates

Returns the balance for a given Date Range for an account and an organization.
Not used anymore, because amf_balance_account_org_flex does the same



