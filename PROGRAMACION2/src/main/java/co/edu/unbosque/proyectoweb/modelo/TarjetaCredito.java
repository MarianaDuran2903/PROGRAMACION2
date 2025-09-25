package co.edu.unbosque.proyectoweb.modelo;

import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity // el profe si nos enseñó a hacer querys que se usaran en el dao
@NamedQueries({
        @NamedQuery(
                name = "TarjetaCredito.findActivasByCliente",
                query = "SELECT t FROM TarjetaCredito t WHERE t.cliente.id = :clienteId AND t.activa = TRUE ORDER BY t.marca"
        ),
        @NamedQuery(
                name = "TarjetaCredito.findByNumero",
                query = "SELECT t FROM TarjetaCredito t WHERE t.numero = :numero"
        )
})
@Table(name = "tarjetas_credito")
public class TarjetaCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable=false, unique=true, length=19)
    private String numero;

    @Column(nullable=false, length=20)
    private String marca;

    @Column(name="fecha_expiracion", nullable=false)
    private LocalDate fechaExpiracion;

    @Column(nullable=false)
    private Boolean activa = true;

    @OneToMany(mappedBy = "tarjeta", fetch = FetchType.LAZY)
    private Set<Pago> pagos = new HashSet<>();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; } public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public String getNumero() { return numero; } public void setNumero(String numero) { this.numero = numero; }
    public String getMarca() { return marca; } public void setMarca(String marca) { this.marca = marca; }
    public LocalDate getFechaExpiracion() { return fechaExpiracion; } public void setFechaExpiracion(LocalDate fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    public Boolean getActiva() { return activa; } public void setActiva(Boolean activa) { this.activa = activa; }
    public Set<Pago> getPagos() { return pagos; } public void setPagos(Set<Pago> pagos) { this.pagos = pagos; }
}
