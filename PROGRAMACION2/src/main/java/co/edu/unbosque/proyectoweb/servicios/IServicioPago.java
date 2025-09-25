package co.edu.unbosque.proyectoweb.servicios;

import co.edu.unbosque.proyectoweb.modelo.excepciones.EntidadNoEncontradaException;
import co.edu.unbosque.proyectoweb.modelo.excepciones.ExcepcionNegocio;
import co.edu.unbosque.proyectoweb.modelo.persistencia.CuotaDTO;
import co.edu.unbosque.proyectoweb.modelo.persistencia.EntradaPagoDTO;
import co.edu.unbosque.proyectoweb.modelo.persistencia.RespuestaPagoDTO;

import java.math.BigDecimal;
import java.util.List;

public interface IServicioPago {

    // Registrar un pago según las reglas del taller
    RespuestaPagoDTO registrarPago(EntradaPagoDTO entrada) throws ExcepcionNegocio, EntidadNoEncontradaException;

    // Obtener un pago por id
    RespuestaPagoDTO obtenerPagoPorId(Long id);

    // Listar todos los pagos (para la tabla principal)
    List<RespuestaPagoDTO> listarPagos();

    // Buscar por código transacción (like) y/o estado
    List<RespuestaPagoDTO> buscarPorFiltros(String codigo, String estado);

    // Calcular tabla de amortización (informativa)
    List<CuotaDTO> calcularTablaAmortizacion(BigDecimal monto, int numeroCuotas, BigDecimal tasaMensual);
}
