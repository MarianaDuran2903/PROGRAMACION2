package co.edu.unbosque.proyectoweb.modelo.persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RespuestaPagoDTO implements Serializable {
    private Long id;
    private String estado;                   // APROBADA | RECHAZADA
    private String codigoTransaccion;        // 12 caracteres si aprobada
    private String numeroTarjetaEnmascarado; // ej. 411111******1111

    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal iva;
    private BigDecimal total;

    private Integer numeroCuotas;
    private LocalDateTime fechaHora;

    private String nombreCompletoCliente;
    private String comercio;

    private List<CuotaDTO> tablaAmortizacion; // opcional: detalle de cuotas

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCodigoTransaccion() { return codigoTransaccion; }
    public void setCodigoTransaccion(String codigoTransaccion) { this.codigoTransaccion = codigoTransaccion; }

    public String getNumeroTarjetaEnmascarado() { return numeroTarjetaEnmascarado; }
    public void setNumeroTarjetaEnmascarado(String numeroTarjetaEnmascarado) { this.numeroTarjetaEnmascarado = numeroTarjetaEnmascarado; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal iva) { this.iva = iva; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Integer getNumeroCuotas() { return numeroCuotas; }
    public void setNumeroCuotas(Integer numeroCuotas) { this.numeroCuotas = numeroCuotas; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getNombreCompletoCliente() { return nombreCompletoCliente; }
    public void setNombreCompletoCliente(String nombreCompletoCliente) { this.nombreCompletoCliente = nombreCompletoCliente; }

    public String getComercio() { return comercio; }
    public void setComercio(String comercio) { this.comercio = comercio; }

    public List<CuotaDTO> getTablaAmortizacion() { return tablaAmortizacion; }
    public void setTablaAmortizacion(List<CuotaDTO> tablaAmortizacion) { this.tablaAmortizacion = tablaAmortizacion; }
}
