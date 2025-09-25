package co.edu.unbosque.proyectoweb.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;

public class EntradaPagoDTO implements Serializable {
    private String identificacionCliente;
    private Long idTarjeta;
    private BigDecimal subtotal;
    private String comercio;
    private Integer numeroCuotas;

    // Getters y setters
    public String getIdentificacionCliente() { return identificacionCliente; }
    public void setIdentificacionCliente(String identificacionCliente) { this.identificacionCliente = identificacionCliente; }

    public Long getIdTarjeta() { return idTarjeta; }
    public void setIdTarjeta(Long idTarjeta) { this.idTarjeta = idTarjeta; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public String getComercio() { return comercio; }
    public void setComercio(String comercio) { this.comercio = comercio; }

    public Integer getNumeroCuotas() { return numeroCuotas; }
    public void setNumeroCuotas(Integer numeroCuotas) { this.numeroCuotas = numeroCuotas; }
}
