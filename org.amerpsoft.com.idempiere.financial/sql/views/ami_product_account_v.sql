-- View: ami_product_accounts_v

-- DROP VIEW ami_product_accounts_v;

CREATE OR REPLACE VIEW ami_product_accounts_v AS 
SELECT DISTINCT ON (AD_Client_ID, Value)
AD_Client_ID, M_Product_ID, C_ElementValue_ID, Value, Name
FROM (
	--p_revenue_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_revenue_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_expense_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_expense_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_asset_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_asset_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_purchasepricevariance_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_purchasepricevariance_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_invoicepricevariance_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_invoicepricevariance_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_cogs_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_cogs_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_tradediscountrec_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_tradediscountrec_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_tradediscountgrant_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_tradediscountgrant_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_inventoryclearing_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_inventoryclearing_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_costadjustment_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_costadjustment_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_methodchangevariance_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_methodchangevariance_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_wip_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_wip_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_usagevariance_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_usagevariance_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_ratevariance_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_ratevariance_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_mixvariance_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_mixvariance_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_floorstock_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_floorstock_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_costofproduction_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_costofproduction_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_labor_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_labor_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_burden_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_burden_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_outsideprocessing_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_outsideprocessing_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_overhead_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_overhead_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_scrap_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_scrap_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)

	UNION
	--p_averagecostvariance_acct
	SELECT DISTINCT cev.AD_Client_ID, pro.M_Product_ID, cev.C_ElementValue_ID, cev.value , cev.name
	FROM M_Product_Acct pra 
	LEFT JOIN M_Product pro ON (pra.M_Product_ID = pro.M_Product_ID)
	LEFT JOIN C_ValidCombination vac ON (vac.C_ValidCombination_ID = pra.p_averagecostvariance_acct)
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = vac.Account_ID)
  
) pro_comb 
WHERE AD_Client_ID IS NOT NULL AND C_ElementValue_ID IS NOT NULL 
ORDER BY AD_Client_ID, Value;

ALTER TABLE ami_product_accounts_v
  OWNER TO postgres;

