package org.amerp.reports;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * AccountElementBasic
 * Clase para almacenar los resultados de la función adempiere.amf_element_value_tree_basic
 * 
 * @author Luis
 */
public class AccountElementBasic {

    private String pathel;
    private BigDecimal[] ancestry;
    private Integer level;
    private BigDecimal nodeId;
    private BigDecimal parentId;
    private BigDecimal cElementId;
    private BigDecimal cElementValueId;
    private BigDecimal adClientId;
    private String isActive;
    private String codigo;
    private String name;
    private String description;
    private Integer length;
    private String accountType;
    private String accountSign;
    private String isDocControlled;
    private BigDecimal elementCElementId;
    private String isSummary;
    private String[] acctParent;
    private String cliname;
    private String clidescription;
    private byte[] cli_logo;
    private Integer ad_org_id;
    private String value_parent;

    // ===== Constructores =====

    public AccountElementBasic() {
    }

    public AccountElementBasic(
        String pathel,
        BigDecimal[] ancestry,
        Integer level,
        BigDecimal nodeId,
        BigDecimal parentId,
        BigDecimal cElementId,
        BigDecimal cElementValueId,
        BigDecimal adClientId,
        String isActive,
        String codigo,
        String name,
        String description,
        Integer length,
        String accountType,
        String accountSign,
        String isDocControlled,
        BigDecimal elementCElementId,
        String isSummary,
        String[] acctParent
    ) {
        this.pathel = pathel;
        this.ancestry = ancestry;
        this.level = level;
        this.nodeId = nodeId;
        this.parentId = parentId;
        this.cElementId = cElementId;
        this.cElementValueId = cElementValueId;
        this.adClientId = adClientId;
        this.isActive = isActive;
        this.codigo = codigo;
        this.name = name;
        this.description = description;
        this.length = length;
        this.accountType = accountType;
        this.accountSign = accountSign;
        this.isDocControlled = isDocControlled;
        this.elementCElementId = elementCElementId;
        this.isSummary = isSummary;
        this.acctParent = acctParent;
    }

    // ===== Getters y Setters =====

    public String getPathel() { return pathel; }
    public void setPathel(String pathel) { this.pathel = pathel; }

    public BigDecimal[] getAncestry() { return ancestry; }
    public void setAncestry(BigDecimal[] ancestry) { this.ancestry = ancestry; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public BigDecimal getNodeId() { return nodeId; }
    public void setNodeId(BigDecimal nodeId) { this.nodeId = nodeId; }

    public BigDecimal getParentId() { return parentId; }
    public void setParentId(BigDecimal parentId) { this.parentId = parentId; }

    public BigDecimal getCElementId() { return cElementId; }
    public void setCElementId(BigDecimal cElementId) { this.cElementId = cElementId; }

    public BigDecimal getCElementValueId() { return cElementValueId; }
    public void setCElementValueId(BigDecimal cElementValueId) { this.cElementValueId = cElementValueId; }

    public BigDecimal getAdClientId() { return adClientId; }
    public void setAdClientId(BigDecimal adClientId) { this.adClientId = adClientId; }

    public String getIsActive() { return isActive; }
    public void setIsActive(String isActive) { this.isActive = isActive; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getLength() { return length; }
    public void setLength(Integer length) { this.length = length; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getAccountSign() { return accountSign; }
    public void setAccountSign(String accountSign) { this.accountSign = accountSign; }

    public String getIsDocControlled() { return isDocControlled; }
    public void setIsDocControlled(String isDocControlled) { this.isDocControlled = isDocControlled; }

    public BigDecimal getElementCElementId() { return elementCElementId; }
    public void setElementCElementId(BigDecimal elementCElementId) { this.elementCElementId = elementCElementId; }

    public String getIsSummary() { return isSummary; }
    public void setIsSummary(String isSummary) { this.isSummary = isSummary; }

    public String[] getAcctParent() { return acctParent; }
    public void setAcctParent(String[] acctParent) { this.acctParent = acctParent; }

    public String getCliname() { return cliname; }
    public void setCliname(String cliname) { this.cliname = cliname; }

    public String getClidescription() { return clidescription; }
    public void setClidescription(String clidescription) { this.clidescription = clidescription; }

    public byte[] getCli_logo() { return cli_logo; }
    public void setCli_logo(byte[] cli_logo) { this.cli_logo = cli_logo; }

    public Integer getAd_org_id() { return ad_org_id; }
    public void setAd_org_id(Integer ad_org_id) { this.ad_org_id = ad_org_id; }

    public String getValue_parent() { return value_parent; }
    public void setValue_parent(String value_parent) { this.value_parent = value_parent; }

    @Override
    public String toString() {
        return "AccountElementBasic{" +
                "pathel='" + pathel + '\'' +
                ", ancestry=" + Arrays.toString(ancestry) +
                ", level=" + level +
                ", nodeId=" + nodeId +
                ", parentId=" + parentId +
                ", cElementId=" + cElementId +
                ", cElementValueId=" + cElementValueId +
                ", adClientId=" + adClientId +
                ", isActive='" + isActive + '\'' +
                ", codigo='" + codigo + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", length=" + length +
                ", accountType='" + accountType + '\'' +
                ", accountSign='" + accountSign + '\'' +
                ", isDocControlled='" + isDocControlled + '\'' +
                ", elementCElementId=" + elementCElementId +
                ", isSummary='" + isSummary + '\'' +
                ", acctParent=" + Arrays.toString(acctParent) +
                '}';
    }
}
