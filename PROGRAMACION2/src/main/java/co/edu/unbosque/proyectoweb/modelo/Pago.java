package co.edu.unbosque.proyectoweb.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Pago.findAll",
                query = "SELECT p FROM Pago p ORDER BY p.fechaHora DESC"
        ),
        @NamedQuery(
                name = "Pago.findByCodigoTransaccionLike",
                query = "SELECT p FROM Pago p WHERE p.codigoTransaccion LIKE :codigo ORDER BY p.fechaHora DESC"
        ),
        @NamedQuery(
                name = "Pago.findByEstado",
                query = "SELECT p FROM Pago p WHERE p.estado = :estado ORDER BY p.fechaHora DESC"
        ),
        @NamedQuery(
                name = "Pago.findByCodigoAndEstado",
                query = "SELECT p FROM Pago p WHERE p.codigoTransaccion LIKE :codigo AND p.estado = :estado ORDER BY p.fechaHora DESC"
        ),
        @NamedQuery(
                name = "Pago.countByCliente",
                query = "SELECT COUNT(p) FROM Pago p WHERE p.cliente.id = :clienteId"
        )
})
@Table(
        name = "pagos",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_pago_unico",
                columnNames = {"tarjeta_id","cliente_id","comercio","fecha_hora"}
        )
)

public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="cliente_id")
    private Cliente cliente;

    @ManyToOne(optional=false)
    @JoinColumn(name="tarjeta_id")
    private TarjetaCredito tarjeta;

    @Column(nullable=false, length=120)// el profe no nos enseño sobre lo que va dento del @column
    // pero segun entiendo, le da mas rigidez para q no hayan errores, no se son innecesarios
    private String comercio;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal subtotal;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal descuento;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal iva;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal total;

    @Column(name="numero_cuotas", nullable=false)
    private Integer numeroCuotas;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=12)
    private EstadoTransaccion estado;

    @Column(name="codigo_transaccion", length=12, unique = true)
    private String codigoTransaccion;  // solo si APROBADA

    @Column(name="fecha_hora", nullable=false)
    private LocalDateTime fechaHora;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; } public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public TarjetaCredito getTarjeta() { return tarjeta; } public void setTarjeta(TarjetaCredito tarjeta) { this.tarjeta = tarjeta; }
    public String getComercio() { return comercio; } public void setComercio(String comercio) { this.comercio = comercio; }
    public BigDecimal getSubtotal() { return subtotal; } public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getDescuento() { return descuento; } public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
    public BigDecimal getIva() { return iva; } public void setIva(BigDecimal iva) { this.iva = iva; }
    public BigDecimal getTotal() { return total; } public void setTotal(BigDecimal total) { this.total = total; }
    public Integer getNumeroCuotas() { return numeroCuotas; } public void setNumeroCuotas(Integer numeroCuotas) { this.numeroCuotas = numeroCuotas; }
    public EstadoTransaccion getEstado() { return estado; } public void setEstado(EstadoTransaccion estado) { this.estado = estado; }
    public String getCodigoTransaccion() { return codigoTransaccion; } public void setCodigoTransaccion(String codigoTransaccion) { this.codigoTransaccion = codigoTransaccion; }
    public LocalDateTime getFechaHora() { return fechaHora; } public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}