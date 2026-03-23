package org.amerp.reports;

import java.math.BigDecimal;

/**
 * Entidad para Balance de Comprobación Comparativo de 12 periodos.
 */
public class TrialBalanceLine12 {
    
    // CAMPOS DE JERARQUÍA Y CÓDIGO
    private String codigo;
    private String nombre;
    private Integer ad_org_id;
    private String orgValue;      
    private String tipoRegistro;  
    private String accounttype;
    private String accountsign;
    private String issummary;
    private int level;

    // SALDOS DE MOVIMIENTOS POR PERIODO (sa_p01 a sa_p12 y sa_anual)
    private BigDecimal sa_p01;
    private BigDecimal sa_p02;
    private BigDecimal sa_p03;
    private BigDecimal sa_p04;
    private BigDecimal sa_p05;
    private BigDecimal sa_p06;
    private BigDecimal sa_p07;
    private BigDecimal sa_p08;
    private BigDecimal sa_p09;
    private BigDecimal sa_p10;
    private BigDecimal sa_p11;
    private BigDecimal sa_p12;
    private BigDecimal sa_anual;

    // SALDOS FINALES POR PERIODO (bal_p01 a bal_p12 y bal_anual)
    private BigDecimal bal_p01;
    private BigDecimal bal_p02;
    private BigDecimal bal_p03;
    private BigDecimal bal_p04;
    private BigDecimal bal_p05;
    private BigDecimal bal_p06;
    private BigDecimal bal_p07;
    private BigDecimal bal_p08;
    private BigDecimal bal_p09;
    private BigDecimal bal_p10;
    private BigDecimal bal_p11;
    private BigDecimal bal_p12;
    private BigDecimal bal_anual;

    public TrialBalanceLine12() {
        // Inicializar BigDecimals en ZERO para evitar NullPointerExceptions en operaciones
        BigDecimal zero = BigDecimal.ZERO;
        this.sa_p01 = this.sa_p02 = this.sa_p03 = this.sa_p04 = this.sa_p05 = this.sa_p06 = 
        this.sa_p07 = this.sa_p08 = this.sa_p09 = this.sa_p10 = this.sa_p11 = this.sa_p12 = this.sa_anual = zero;
        
        this.bal_p01 = this.bal_p02 = this.bal_p03 = this.bal_p04 = this.bal_p05 = this.bal_p06 = 
        this.bal_p07 = this.bal_p08 = this.bal_p09 = this.bal_p10 = this.bal_p11 = this.bal_p12 = this.bal_anual = zero;
    }

    // --- Getters y Setters ---

    // Jerarquía y Metadatos
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getAD_Org_ID() { return ad_org_id; }
    public void setAD_Org_ID(Integer ad_org_id) { this.ad_org_id = ad_org_id; }
    public String getOrgValue() { return orgValue; }
    public void setOrgValue(String orgValue) { this.orgValue = orgValue; }
    public String getTipoRegistro() { return tipoRegistro; }
    public void setTipoRegistro(String tipoRegistro) { this.tipoRegistro = tipoRegistro; }
    public String getAccountType() { return accounttype; }
    public void setAccountType(String accounttype) { this.accounttype = accounttype; }
    public String getAccountSign() { return accountsign; }
    public void setAccountSign(String accountsign) { this.accountsign = accountsign; }
    public String getIsSummary() { return issummary; }
    public void setIsSummary(String issummary) { this.issummary = issummary; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    // Getters y Setters: Movimientos (sa_pxx)
    public BigDecimal getSa_p01() { return sa_p01; }
    public void setSa_p01(BigDecimal sa_p01) { this.sa_p01 = sa_p01; }
    public BigDecimal getSa_p02() { return sa_p02; }
    public void setSa_p02(BigDecimal sa_p02) { this.sa_p02 = sa_p02; }
    public BigDecimal getSa_p03() { return sa_p03; }
    public void setSa_p03(BigDecimal sa_p03) { this.sa_p03 = sa_p03; }
    public BigDecimal getSa_p04() { return sa_p04; }
    public void setSa_p04(BigDecimal sa_p04) { this.sa_p04 = sa_p04; }
    public BigDecimal getSa_p05() { return sa_p05; }
    public void setSa_p05(BigDecimal sa_p05) { this.sa_p05 = sa_p05; }
    public BigDecimal getSa_p06() { return sa_p06; }
    public void setSa_p06(BigDecimal sa_p06) { this.sa_p06 = sa_p06; }
    public BigDecimal getSa_p07() { return sa_p07; }
    public void setSa_p07(BigDecimal sa_p07) { this.sa_p07 = sa_p07; }
    public BigDecimal getSa_p08() { return sa_p08; }
    public void setSa_p08(BigDecimal sa_p08) { this.sa_p08 = sa_p08; }
    public BigDecimal getSa_p09() { return sa_p09; }
    public void setSa_p09(BigDecimal sa_p09) { this.sa_p09 = sa_p09; }
    public BigDecimal getSa_p10() { return sa_p10; }
    public void setSa_p10(BigDecimal sa_p10) { this.sa_p10 = sa_p10; }
    public BigDecimal getSa_p11() { return sa_p11; }
    public void setSa_p11(BigDecimal sa_p11) { this.sa_p11 = sa_p11; }
    public BigDecimal getSa_p12() { return sa_p12; }
    public void setSa_p12(BigDecimal sa_p12) { this.sa_p12 = sa_p12; }
    public BigDecimal getSa_anual() { return sa_anual; }
    public void setSa_anual(BigDecimal sa_anual) { this.sa_anual = sa_anual; }

    // Getters y Setters: Saldos Finales (bal_pxx)
    public BigDecimal getBal_p01() { return bal_p01; }
    public void setBal_p01(BigDecimal bal_p01) { this.bal_p01 = bal_p01; }
    public BigDecimal getBal_p02() { return bal_p02; }
    public void setBal_p02(BigDecimal bal_p02) { this.bal_p02 = bal_p02; }
    public BigDecimal getBal_p03() { return bal_p03; }
    public void setBal_p03(BigDecimal bal_p03) { this.bal_p03 = bal_p03; }
    public BigDecimal getBal_p04() { return bal_p04; }
    public void setBal_p04(BigDecimal bal_p04) { this.bal_p04 = bal_p04; }
    public BigDecimal getBal_p05() { return bal_p05; }
    public void setBal_p05(BigDecimal bal_p05) { this.bal_p05 = bal_p05; }
    public BigDecimal getBal_p06() { return bal_p06; }
    public void setBal_p06(BigDecimal bal_p06) { this.bal_p06 = bal_p06; }
    public BigDecimal getBal_p07() { return bal_p07; }
    public void setBal_p07(BigDecimal bal_p07) { this.bal_p07 = bal_p07; }
    public BigDecimal getBal_p08() { return bal_p08; }
    public void setBal_p08(BigDecimal bal_p08) { this.bal_p08 = bal_p08; }
    public BigDecimal getBal_p09() { return bal_p09; }
    public void setBal_p09(BigDecimal bal_p09) { this.bal_p09 = bal_p09; }
    public BigDecimal getBal_p10() { return bal_p10; }
    public void setBal_p10(BigDecimal bal_p10) { this.bal_p10 = bal_p10; }
    public BigDecimal getBal_p11() { return bal_p11; }
    public void setBal_p11(BigDecimal bal_p11) { this.bal_p11 = bal_p11; }
    public BigDecimal getBal_p12() { return bal_p12; }
    public void setBal_p12(BigDecimal bal_p12) { this.bal_p12 = bal_p12; }
    public BigDecimal getBal_anual() { return bal_anual; }
    public void setBal_anual(BigDecimal bal_anual) { this.bal_anual = bal_anual; }
}