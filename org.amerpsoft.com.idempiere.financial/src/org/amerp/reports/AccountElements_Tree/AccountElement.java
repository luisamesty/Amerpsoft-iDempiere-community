package org.amerp.reports.AccountElements_Tree;

import java.math.BigDecimal; // Importa BigDecimal si no lo tienes ya

public class AccountElement {

    private Integer level;
    private BigDecimal node_id;
    private BigDecimal parent_id;
    private String clivalue;
    private String cliname;
    private String clidescription;
    private byte[] cli_logo; // Para campos de tipo byte[] como imágenes
    private BigDecimal c_element_id;
    private BigDecimal c_elementvalue_id;
    private BigDecimal ad_client_id;
    private BigDecimal ad_org_id;
    private String isactive;
    private String value;
    private String name;
    private String description;
    private Integer length;
    private String accounttype;
    private String accountsign;
    private String isdoccontrolled;
    private BigDecimal COLUMN_20; // Asegúrate de que este campo esté correctamente mapeado si no es c_element_id duplicado
    private String issummary;
    private String value_parent;
    private String value1;
    private String value2;
    private String value3;
    private String value4;
    private String value5;
    private String value6;
    private String value7;
    private String value8;
    private String value9;

    // Constructor (opcional, pero útil para inicializar objetos)
    public AccountElement(Integer level, BigDecimal node_id, BigDecimal parent_id, String clivalue, String cliname,
                          String clidescription, byte[] cli_logo, BigDecimal c_element_id, BigDecimal c_elementvalue_id,
                          BigDecimal ad_client_id, BigDecimal ad_org_id, String isactive, String value, String name,
                          String description, Integer length, String accounttype, String accountsign,
                          String isdoccontrolled, BigDecimal COLUMN_20, String issummary, String value_parent,
                          String value1, String value2, String value3, String value4, String value5,
                          String value6, String value7, String value8, String value9) {
        this.level = level;
        this.node_id = node_id;
        this.parent_id = parent_id;
        this.clivalue = clivalue;
        this.cliname = cliname;
        this.clidescription = clidescription;
        this.cli_logo = cli_logo;
        this.c_element_id = c_element_id;
        this.c_elementvalue_id = c_elementvalue_id;
        this.ad_client_id = ad_client_id;
        this.ad_org_id = ad_org_id;
        this.isactive = isactive;
        this.value = value;
        this.name = name;
        this.description = description;
        this.length = length;
        this.accounttype = accounttype;
        this.accountsign = accountsign;
        this.isdoccontrolled = isdoccontrolled;
        this.COLUMN_20 = COLUMN_20;
        this.issummary = issummary;
        this.value_parent = value_parent;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
        this.value7 = value7;
        this.value8 = value8;
        this.value9 = value9;
    }

    // Constructor vacío (necesario para algunas bibliotecas de serialización/deserialización)
    public AccountElement() {
    }

    // --- Getters y Setters para cada campo ---

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public BigDecimal getNode_id() { return node_id; }
    public void setNode_id(BigDecimal node_id) { this.node_id = node_id; }

    public BigDecimal getParent_id() { return parent_id; }
    public void setParent_id(BigDecimal parent_id) { this.parent_id = parent_id; }

    public String getClivalue() { return clivalue; }
    public void setClivalue(String clivalue) { this.clivalue = clivalue; }

    public String getCliname() { return cliname; }
    public void setCliname(String cliname) { this.cliname = cliname; }

    public String getClidescription() { return clidescription; }
    public void setClidescription(String clidescription) { this.clidescription = clidescription; }

    public byte[] getCli_logo() { return cli_logo; }
    public void setCli_logo(byte[] cli_logo) { this.cli_logo = cli_logo; }

    public BigDecimal getC_element_id() { return c_element_id; }
    public void setC_element_id(BigDecimal c_element_id) { this.c_element_id = c_element_id; }

    public BigDecimal getC_elementvalue_id() { return c_elementvalue_id; }
    public void setC_elementvalue_id(BigDecimal c_elementvalue_id) { this.c_elementvalue_id = c_elementvalue_id; }

    public BigDecimal getAd_client_id() { return ad_client_id; }
    public void setAd_client_id(BigDecimal ad_client_id) { this.ad_client_id = ad_client_id; }

    public BigDecimal getAd_org_id() { return ad_org_id; }
    public void setAd_org_id(BigDecimal ad_org_id) { this.ad_org_id = ad_org_id; }

    public String getIsactive() { return isactive; }
    public void setIsactive(String isactive) { this.isactive = isactive; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getLength() { return length; }
    public void setLength(Integer length) { this.length = length; }

    public String getAccounttype() { return accounttype; }
    public void setAccounttype(String accounttype) { this.accounttype = accounttype; }

    public String getAccountsign() { return accountsign; }
    public void setAccountsign(String accountsign) { this.accountsign = accountsign; }

    public String getIsdoccontrolled() { return isdoccontrolled; }
    public void setIsdoccontrolled(String isdoccontrolled) { this.isdoccontrolled = isdoccontrolled; }

    public BigDecimal getCOLUMN_20() { return COLUMN_20; }
    public void setCOLUMN_20(BigDecimal COLUMN_20) { this.COLUMN_20 = COLUMN_20; }

    public String getIssummary() { return issummary; }
    public void setIssummary(String issummary) { this.issummary = issummary; }

    public String getValue_parent() { return value_parent; }
    public void setValue_parent(String value_parent) { this.value_parent = value_parent; }

    public String getValue1() { return value1; }
    public void setValue1(String value1) { this.value1 = value1; }

    public String getValue2() { return value2; }
    public void setValue2(String value2) { this.value2 = value2; }

    public String getValue3() { return value3; }
    public void setValue3(String value3) { this.value3 = value3; }

    public String getValue4() { return value4; }
    public void setValue4(String value4) { this.value4 = value4; }

    public String getValue5() { return value5; }
    public void setValue5(String value5) { this.value5 = value5; }

    public String getValue6() { return value6; }
    public void setValue6(String value6) { this.value6 = value6; }

    public String getValue7() { return value7; }
    public void setValue7(String value7) { this.value7 = value7; }

    public String getValue8() { return value8; }
    public void setValue8(String value8) { this.value8 = value8; }

    public String getValue9() { return value9; }
    public void setValue9(String value9) { this.value9 = value9; }
}