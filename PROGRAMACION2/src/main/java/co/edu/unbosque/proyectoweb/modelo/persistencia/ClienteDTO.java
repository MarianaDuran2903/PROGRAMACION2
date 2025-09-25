package co.edu.unbosque.proyectoweb.modelo.persistencia;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(
    name = "clientes",
    uniqueConstraints = @UniqueConstraint(name = "uk_clientes_identificacion", columnNames = "identificacion")
)
public class ClienteDTO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String identificacion;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(length = 120)
    private String email;

    @Column(length = 30)
    private String telefono;

    public ClienteDTO() {}

    // --- Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}