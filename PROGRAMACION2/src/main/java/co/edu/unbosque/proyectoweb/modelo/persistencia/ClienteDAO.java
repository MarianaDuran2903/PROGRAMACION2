package co.edu.unbosque.proyectoweb.modelo.persistencia;

import co.edu.unbosque.proyectoweb.modelo.Cliente;

import java.util.List;

public interface ClienteDAO {
    Cliente save(Cliente c);
    Cliente findById(Long id);
    Cliente findByIdentificacion(String identificacion);
    List<Cliente> listar();
    Long insertar(ClienteDTO cliente) throws Exception;
}
