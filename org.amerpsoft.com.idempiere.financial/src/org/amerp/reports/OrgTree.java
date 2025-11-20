package org.amerp.reports;


import java.math.BigDecimal;

public class OrgTree {

    private BigDecimal adClientId;
    private BigDecimal adOrgId;
    private BigDecimal adOrgParentId;
    private String isSummary;
    private String orgValue;
    private String orgDescription;
    private String orgName;
    private String allOrgs;
    private String orgTaxId;
    private byte[] orgLogo;

    // --- Getters y Setters
    public BigDecimal getAdClientId() { return adClientId; }
    public void setAdClientId(BigDecimal adClientId) { this.adClientId = adClientId; }

    public BigDecimal getAdOrgId() { return adOrgId; }
    public void setAdOrgId(BigDecimal adOrgId) { this.adOrgId = adOrgId; }

    public BigDecimal getAdOrgParentId() { return adOrgParentId; }
    public void setAdOrgParentId(BigDecimal adOrgParentId) { this.adOrgParentId = adOrgParentId; }

    public String getIsSummary() { return isSummary; }
    public void setIsSummary(String isSummary) { this.isSummary = isSummary; }

    public String getOrgValue() { return orgValue; }
    public void setOrgValue(String orgValue) { this.orgValue = orgValue; }

    public String getOrgDescription() { return orgDescription; }
    public void setOrgDescription(String orgDescription) { this.orgDescription = orgDescription; }

    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }

    public String getAllOrgs() { return allOrgs; }
    public void setAllOrgs(String allOrgs) { this.allOrgs = allOrgs; }

    public String getOrgTaxId() { return orgTaxId; }
    public void setOrgTaxId(String orgTaxId) { this.orgTaxId = orgTaxId; }

    public byte[] getOrgLogo() { return orgLogo; }
    public void setOrgLogo(byte[] orgLogo) { this.orgLogo = orgLogo; }
}
