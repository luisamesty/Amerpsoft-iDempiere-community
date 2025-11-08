package org.amerp.reports;

import java.math.BigDecimal;

public class TrialBalanceLine {
    
    // CAMPOS DE JERARQUÍA Y CÓDIGO
    private String codigo;
    private String nombre;
    private Integer ad_org_id;
    private String orgValue;      
    private String tipoRegistro;  // '10', '60', '50'
    private String accounttype;
    private String accountsign;
    private String issummary;
    
    private int level;            // Nivel jerárquico

    // CAMPOS DE SALDO
    private BigDecimal openBalance;      // openbalance
    private BigDecimal amtAcctDr;        // debitos
    private BigDecimal amtAcctCr;        // creditos
    private BigDecimal balancePeriodo;   // balance_periodo (amtacctsa)
    private BigDecimal closeBalance;     // closebalance

    // Constructor vacío
    public TrialBalanceLine() {}
    
    // --- Getters y Setters ---

    // Código y Nombres
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Saldo
    public BigDecimal getOpenBalance() { return openBalance; }
    public void setOpenBalance(BigDecimal openBalance) { this.openBalance = openBalance; }

    public BigDecimal getAmtAcctDr() { return amtAcctDr; }
    public void setAmtAcctDr(BigDecimal amtAcctDr) { this.amtAcctDr = amtAcctDr; }

    public BigDecimal getAmtAcctCr() { return amtAcctCr; }
    public void setAmtAcctCr(BigDecimal amtAcctCr) { this.amtAcctCr = amtAcctCr; }

    public BigDecimal getBalancePeriodo() { return balancePeriodo; }
    public void setBalancePeriodo(BigDecimal balancePeriodo) { this.balancePeriodo = balancePeriodo; }

    public BigDecimal getCloseBalance() { return closeBalance; }
    public void setCloseBalance(BigDecimal closeBalance) { this.closeBalance = closeBalance; }

    public Integer getAD_org_ID() { return ad_org_id; }
    public void setAD_Org_ID(Integer ad_org_id) { this.ad_org_id = ad_org_id; }

    public String getAccountType() { return accounttype; }
    public void setAccountType(String accounttype) { this.orgValue = accounttype; }
    public String getAccountSign() { return accountsign; }
    public void setAccountSign(String accountsign) { this.orgValue = accountsign; }
    public String getIsSummary() { return issummary; }
    public void setIsSummary(String issummary) { this.orgValue = issummary; }
    public String getOrgValue() { return orgValue; }
    public void setOrgValue(String orgValue) { this.orgValue = orgValue; }

    public String getTipoRegistro() { return tipoRegistro; }
    public void setTipoRegistro(String tipoRegistro) { this.tipoRegistro = tipoRegistro; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
}