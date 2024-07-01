DELETE 
FROM AD_ToolBarButton
WHERE AD_Tab_ID IN (
	SELECT AD_Tab_ID FROM AD_Tab WHERE AD_Window_ID IN (
		SELECT AD_Window_ID FROM AD_Window WHERE SUBSTRING(Name,1,21) = 'Personnel and Payroll'
	)
)
