package org.amerp.reports;

import java.util.Arrays;

public class AccountElementExtended {

    private Integer level;
    private Integer nodeId;
    private Integer parentId;
    private Integer cElementId;
    private Integer cElementValueId;
    private Integer adClientId;
    private String isActive;
    private String codigo;
    private String name;
    private String description;
    private Integer length;
    private String accountType;
    private String accountSign;
    private String isDocControlled;
    private String isSummary;
    private String[] acctParent;
    private String pathEl;
    private Integer[] ancestry;

    // Niveles 0 a 9
    private String codigo0, name0, description0, isSummary0;
    private String codigo1, name1, description1, isSummary1;
    private String codigo2, name2, description2, isSummary2;
    private String codigo3, name3, description3, isSummary3;
    private String codigo4, name4, description4, isSummary4;
    private String codigo5, name5, description5, isSummary5;
    private String codigo6, name6, description6, isSummary6;
    private String codigo7, name7, description7, isSummary7;
    private String codigo8, name8, description8, isSummary8;
    private String codigo9, name9, description9, isSummary9;

    // --- Getters y Setters ---

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getNodeId() { return nodeId; }
    public void setNodeId(Integer nodeId) { this.nodeId = nodeId; }

    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }

    public Integer getcElementId() { return cElementId; }
    public void setcElementId(Integer cElementId) { this.cElementId = cElementId; }

    public Integer getcElementValueId() { return cElementValueId; }
    public void setcElementValueId(Integer cElementValueId) { this.cElementValueId = cElementValueId; }

    public Integer getAdClientId() { return adClientId; }
    public void setAdClientId(Integer adClientId) { this.adClientId = adClientId; }

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

    public String getIsSummary() { return isSummary; }
    public void setIsSummary(String isSummary) { this.isSummary = isSummary; }

    public String[] getAcctParent() { return acctParent; }
    public void setAcctParent(String[] acctParent) { this.acctParent = acctParent; }

    public String getPathEl() { return pathEl; }
    public void setPathEl(String pathEl) { this.pathEl = pathEl; }

    public Integer[] getAncestry() { return ancestry; }
    public void setAncestry(Integer[] ancestry) { this.ancestry = ancestry; }

    // --- Campos jerárquicos ---

    public String getCodigo0() { return codigo0; }
    public void setCodigo0(String codigo0) { this.codigo0 = codigo0; }
    public String getName0() { return name0; }
    public void setName0(String name0) { this.name0 = name0; }
    public String getDescription0() { return description0; }
    public void setDescription0(String description0) { this.description0 = description0; }
    public String getIsSummary0() { return isSummary0; }
    public void setIsSummary0(String isSummary0) { this.isSummary0 = isSummary0; }

    public String getCodigo1() { return codigo1; }
    public void setCodigo1(String codigo1) { this.codigo1 = codigo1; }
    public String getName1() { return name1; }
    public void setName1(String name1) { this.name1 = name1; }
    public String getDescription1() { return description1; }
    public void setDescription1(String description1) { this.description1 = description1; }
    public String getIsSummary1() { return isSummary1; }
    public void setIsSummary1(String isSummary1) { this.isSummary1 = isSummary1; }

    public String getCodigo2() { return codigo2; }
    public void setCodigo2(String codigo2) { this.codigo2 = codigo2; }
    public String getName2() { return name2; }
    public void setName2(String name2) { this.name2 = name2; }
    public String getDescription2() { return description2; }
    public void setDescription2(String description2) { this.description2 = description2; }
    public String getIsSummary2() { return isSummary2; }
    public void setIsSummary2(String isSummary2) { this.isSummary2 = isSummary2; }

    public String getCodigo3() { return codigo3; }
    public void setCodigo3(String codigo3) { this.codigo3 = codigo3; }
    public String getName3() { return name3; }
    public void setName3(String name3) { this.name3 = name3; }
    public String getDescription3() { return description3; }
    public void setDescription3(String description3) { this.description3 = description3; }
    public String getIsSummary3() { return isSummary3; }
    public void setIsSummary3(String isSummary3) { this.isSummary3 = isSummary3; }

    public String getCodigo4() { return codigo4; }
    public void setCodigo4(String codigo4) { this.codigo4 = codigo4; }
    public String getName4() { return name4; }
    public void setName4(String name4) { this.name4 = name4; }
    public String getDescription4() { return description4; }
    public void setDescription4(String description4) { this.description4 = description4; }
    public String getIsSummary4() { return isSummary4; }
    public void setIsSummary4(String isSummary4) { this.isSummary4 = isSummary4; }

    public String getCodigo5() { return codigo5; }
    public void setCodigo5(String codigo5) { this.codigo5 = codigo5; }
    public String getName5() { return name5; }
    public void setName5(String name5) { this.name5 = name5; }
    public String getDescription5() { return description5; }
    public void setDescription5(String description5) { this.description5 = description5; }
    public String getIsSummary5() { return isSummary5; }
    public void setIsSummary5(String isSummary5) { this.isSummary5 = isSummary5; }

    public String getCodigo6() { return codigo6; }
    public void setCodigo6(String codigo6) { this.codigo6 = codigo6; }
    public String getName6() { return name6; }
    public void setName6(String name6) { this.name6 = name6; }
    public String getDescription6() { return description6; }
    public void setDescription6(String description6) { this.description6 = description6; }
    public String getIsSummary6() { return isSummary6; }
    public void setIsSummary6(String isSummary6) { this.isSummary6 = isSummary6; }

    public String getCodigo7() { return codigo7; }
    public void setCodigo7(String codigo7) { this.codigo7 = codigo7; }
    public String getName7() { return name7; }
    public void setName7(String name7) { this.name7 = name7; }
    public String getDescription7() { return description7; }
    public void setDescription7(String description7) { this.description7 = description7; }
    public String getIsSummary7() { return isSummary7; }
    public void setIsSummary7(String isSummary7) { this.isSummary7 = isSummary7; }

    public String getCodigo8() { return codigo8; }
    public void setCodigo8(String codigo8) { this.codigo8 = codigo8; }
    public String getName8() { return name8; }
    public void setName8(String name8) { this.name8 = name8; }
    public String getDescription8() { return description8; }
    public void setDescription8(String description8) { this.description8 = description8; }
    public String getIsSummary8() { return isSummary8; }
    public void setIsSummary8(String isSummary8) { this.isSummary8 = isSummary8; }

    public String getCodigo9() { return codigo9; }
    public void setCodigo9(String codigo9) { this.codigo9 = codigo9; }
    public String getName9() { return name9; }
    public void setName9(String name9) { this.name9 = name9; }
    public String getDescription9() { return description9; }
    public void setDescription9(String description9) { this.description9 = description9; }
    public String getIsSummary9() { return isSummary9; }
    public void setIsSummary9(String isSummary9) { this.isSummary9 = isSummary9; }

    @Override
    public String toString() {
        return "AccountElementExtended{" +
                "level=" + level +
                ", nodeId=" + nodeId +
                ", codigo='" + codigo + '\'' +
                ", name='" + name + '\'' +
                ", acctParent=" + Arrays.toString(acctParent) +
                '}';
    }
}
