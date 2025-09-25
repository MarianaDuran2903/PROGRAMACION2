package co.edu.unbosque.proyectoweb.modelo.persistencia;

import co.edu.unbosque.proyectoweb.modelo.EstadoTransaccion;
import co.edu.unbosque.proyectoweb.modelo.Pago;

import java.util.List;

public interface PagoDAO {
    Pago save(Pago p);
    Pago findById(Long id);
    List<Pago> findAll();
    List<Pago> findByCodigoTransaccionLike(String codigoLike);
    List<Pago> findByEstado(EstadoTransaccion estado);
    List<Pago> findByCodigoAndEstado(String codigoLike, EstadoTransaccion estado);
    long countByCliente(Long clienteId);
}
