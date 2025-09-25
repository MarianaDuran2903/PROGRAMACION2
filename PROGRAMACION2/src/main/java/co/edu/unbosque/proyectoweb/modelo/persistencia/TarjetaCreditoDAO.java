package co.edu.unbosque.proyectoweb.modelo.persistencia;

import co.edu.unbosque.proyectoweb.modelo.TarjetaCredito;

import java.util.List;

public interface TarjetaCreditoDAO {
    TarjetaCredito save(TarjetaCredito t);
    TarjetaCredito findById(Long id);
    TarjetaCredito findByNumero(String numero);
    List<TarjetaCredito> findActivasByCliente(Long clienteId);
}
