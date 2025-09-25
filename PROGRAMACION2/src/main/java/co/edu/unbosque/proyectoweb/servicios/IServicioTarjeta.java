package co.edu.unbosque.proyectoweb.servicios;

import co.edu.unbosque.proyectoweb.modelo.excepciones.EntidadNoEncontradaException;
import co.edu.unbosque.proyectoweb.modelo.excepciones.ExcepcionNegocio;
import co.edu.unbosque.proyectoweb.modelo.persistencia.TarjetaDTO;

import java.util.List;

public interface IServicioTarjeta {
    TarjetaDTO registrar(TarjetaDTO dto) throws ExcepcionNegocio, EntidadNoEncontradaException;                 // registrar tarjeta para un cliente
    TarjetaDTO obtenerPorId(Long id) throws EntidadNoEncontradaException;
    TarjetaDTO obtenerPorNumero(String numero);
    List<TarjetaDTO> listarActivasPorCliente(Long clienteId);
    TarjetaDTO desactivar(Long idTarjeta) throws EntidadNoEncontradaException;                // opcional: poner activa=false
}
