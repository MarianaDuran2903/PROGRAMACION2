package co.edu.unbosque.proyectoweb.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CuotaDTO implements Serializable {
    private Integer numero;
    private BigDecimal valorAPagar;   // cuota fija
    private BigDecimal abonoCapital;  // cuota - intereses
    private BigDecimal intereses;     // saldo_anterior * i
    private BigDecimal saldo;         // saldo restante

    public Integer getNumero() {return numero;}
    public void setNumero(Integer numero) {this.numero = numero;}
    public BigDecimal getValorAPagar() {return valorAPagar;}
    public void setValorAPagar(BigDecimal valorAPagar) {this.valorAPagar = valorAPagar;}
    public BigDecimal getAbonoCapital() {return abonoCapital;}
    public void setAbonoCapital(BigDecimal abonoCapital) {this.abonoCapital = abonoCapital;}
    public BigDecimal getIntereses() {return intereses;}
    public void setIntereses(BigDecimal intereses) {this.intereses = intereses;}
    public BigDecimal getSaldo() {return saldo;}
    public void setSaldo(BigDecimal saldo) {this.saldo = saldo;}
}