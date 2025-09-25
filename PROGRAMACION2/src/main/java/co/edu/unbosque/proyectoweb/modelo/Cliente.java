package co.edu.unbosque.proyectoweb.modelo;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Cliente.findByIdentificacion",
                query = "SELECT c FROM Cliente c WHERE c.identificacion = :ident"
        ),
        @NamedQuery(
                name = "Cliente.listar",
                query = "SELECT c FROM Cliente c ORDER BY c.apellidos, c.nombres"
        )
})
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true) // el profe no nos enseño sobre lo que va dento del @column
    // pero segun entiendo, le da mas rigidez para q no hayan errores
    private String identificacion;

    @Column(nullable=false)
    private String nombres;

    @Column(nullable=false)
    private String apellidos;

    @Column()
    private String email;

    @Column()
    private String telefono;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TarjetaCredito> tarjetas = new HashSet<>();

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private Set<Pago> pagos = new HashSet<>();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getIdentificacion() { return identificacion; } public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public String getNombres() { return nombres; } public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; } public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; } public void setTelefono(String telefono) { this.telefono = telefono; }
    public Set<TarjetaCredito> getTarjetas() { return tarjetas; } public void setTarjetas(Set<TarjetaCredito> tarjetas) { this.tarjetas = tarjetas; }
    public Set<Pago> getPagos() { return pagos; } public void setPagos(Set<Pago> pagos) { this.pagos = pagos; }
}
