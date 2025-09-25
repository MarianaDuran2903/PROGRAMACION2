package co.edu.unbosque.proyectoweb.modelo.persistencia;

import java.io.Serializable;
import java.time.LocalDate;

public class TarjetaDTO implements Serializable {
    private Long id;
    private String numero;            // completo al registrar, enmascarado al mostrar
    private String marca;             // VISA, MASTERCARD...
    private LocalDate fechaExpiracion;
    private Boolean activa;
    private String identificacionCliente; // útil al registrar

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public LocalDate getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDate fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }

    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }

    public String getIdentificacionCliente() { return identificacionCliente; }
    public void setIdentificacionCliente(String identificacionCliente) { this.identificacionCliente = identificacionCliente; }
}
