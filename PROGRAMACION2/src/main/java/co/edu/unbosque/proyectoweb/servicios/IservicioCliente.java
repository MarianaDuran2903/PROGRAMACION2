package co.edu.unbosque.proyectoweb.servicios;

import co.edu.unbosque.proyectoweb.modelo.excepciones.EntidadNoEncontradaException;
import co.edu.unbosque.proyectoweb.modelo.excepciones.ExcepcionNegocio;
import co.edu.unbosque.proyectoweb.modelo.persistencia.ClienteDTO;

import java.util.List;

public interface IservicioCliente {
    ClienteDTO registrar(ClienteDTO dto) throws ExcepcionNegocio;
    ClienteDTO obtenerPorId(Long id) throws EntidadNoEncontradaException;
    ClienteDTO obtenerPorIdentificacion(String identificacion);
    List<ClienteDTO> listar();
    Long crear(ClienteDTO cliente) throws Exception;
}
