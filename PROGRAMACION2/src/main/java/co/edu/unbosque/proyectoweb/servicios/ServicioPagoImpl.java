package co.edu.unbosque.proyectoweb.servicios;

import co.edu.unbosque.proyectoweb.modelo.Cliente;
import co.edu.unbosque.proyectoweb.modelo.EstadoTransaccion;
import co.edu.unbosque.proyectoweb.modelo.Pago;
import co.edu.unbosque.proyectoweb.modelo.TarjetaCredito;
import co.edu.unbosque.proyectoweb.modelo.excepciones.EntidadNoEncontradaException;
import co.edu.unbosque.proyectoweb.modelo.excepciones.ExcepcionNegocio;
import co.edu.unbosque.proyectoweb.modelo.persistencia.*;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Stateless // Este servicio lo hizo principalmete chatgpt porq requeria muchos claculos y detalles!!
public class ServicioPagoImpl implements IServicioPago {

    @Inject private PagoDAO pagoDAO;
    @Inject private ClienteDAO clienteDAO;
    @Inject private TarjetaCreditoDAO tarjetaDAO;

    private final ModelMapper mapper = new ModelMapper();
    private static final MathContext MC = new MathContext(20, RoundingMode.HALF_UP);

    // ---------- Reglas de negocio principales ----------

    @Override
    public RespuestaPagoDTO registrarPago(EntradaPagoDTO entrada) throws ExcepcionNegocio, EntidadNoEncontradaException {
        // Validaciones de entrada
        if (entrada == null) throw new ExcepcionNegocio("Datos de entrada vacíos.");
        if (entrada.getIdentificacionCliente() == null || entrada.getIdentificacionCliente().isBlank())
            throw new ExcepcionNegocio("La identificación del cliente es obligatoria.");
        if (entrada.getIdTarjeta() == null)
            throw new ExcepcionNegocio("Debe seleccionar una tarjeta.");
        if (entrada.getComercio() == null || entrada.getComercio().isBlank())
            throw new ExcepcionNegocio("El nombre del comercio es obligatorio.");
        if (entrada.getSubtotal() == null || entrada.getSubtotal().compareTo(BigDecimal.ZERO) <= 0)
            throw new ExcepcionNegocio("El valor de la compra debe ser mayor a 0.");
        if (entrada.getNumeroCuotas() == null || entrada.getNumeroCuotas() < 1 || entrada.getNumeroCuotas() > 36)
            throw new ExcepcionNegocio("El número de cuotas debe estar entre 1 y 36.");

        // Entidades
        Cliente cliente = clienteDAO.findByIdentificacion(entrada.getIdentificacionCliente());
        if (cliente == null) throw new EntidadNoEncontradaException("Cliente no encontrado.");

        TarjetaCredito tarjeta = tarjetaDAO.findById(entrada.getIdTarjeta());
        if (tarjeta == null) throw new EntidadNoEncontradaException("Tarjeta no encontrada.");
        if (Boolean.FALSE.equals(tarjeta.getActiva()))
            throw new ExcepcionNegocio("La tarjeta está inactiva.");
        if (!tarjeta.getCliente().getId().equals(cliente.getId()))
            throw new ExcepcionNegocio("La tarjeta no pertenece al cliente seleccionado.");

        // (opcional) Validación Luhn del número de tarjeta
        if (!validaLuhn(tarjeta.getNumero()))
            throw new ExcepcionNegocio("El número de la tarjeta no es válido.");

        // Cálculos: IVA 16% y descuento 2% si es primera compra real
        BigDecimal subtotal = entrada.getSubtotal();
        BigDecimal iva = subtotal.multiply(new BigDecimal("0.16"), MC);
        long comprasPrevias = pagoDAO.countByCliente(cliente.getId());
        BigDecimal descuento = (comprasPrevias == 0)
                ? subtotal.multiply(new BigDecimal("0.02"), MC)
                : BigDecimal.ZERO;
        BigDecimal total = subtotal.add(iva).subtract(descuento);

        // Decisión aleatoria APROBADA/RECHAZADA
        boolean aprobada = new Random().nextBoolean();
        if (!aprobada) {
            // Política: NO persistimos pagos rechazados, mostramos mensaje
            throw new ExcepcionNegocio("La transacción fue rechazada.");
        }

        // Si aprobada: construir y persistir Pago
        Pago pago = new Pago();
        pago.setCliente(cliente);
        pago.setTarjeta(tarjeta);
        pago.setComercio(entrada.getComercio());
        pago.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        pago.setIva(iva.setScale(2, RoundingMode.HALF_UP));
        pago.setDescuento(descuento.setScale(2, RoundingMode.HALF_UP));
        pago.setTotal(total.setScale(2, RoundingMode.HALF_UP));
        pago.setNumeroCuotas(entrada.getNumeroCuotas());
        pago.setEstado(EstadoTransaccion.APROBADA);
        pago.setCodigoTransaccion(generarCodigoTransaccion());
        pago.setFechaHora(LocalDateTime.now());

        try {
            pagoDAO.save(pago);
        } catch (PersistenceException pe) {
            // Violación de unique constraint (pago duplicado)
            throw new ExcepcionNegocio(
                    "Pago duplicado: misma tarjeta, cliente, comercio y fecha/hora.");
        }

        // DTO de salida
        RespuestaPagoDTO dto = aDTO(pago);
        return dto;
    }

    @Override
    public RespuestaPagoDTO obtenerPagoPorId(Long id) {
        Pago p = pagoDAO.findById(id);
        if (p == null) return null;
        return aDTO(p);
    }

    @Override
    public List<RespuestaPagoDTO> listarPagos() {
        List<Pago> lista = pagoDAO.findAll();
        List<RespuestaPagoDTO> out = new ArrayList<>();
        for (Pago p : lista) out.add(aDTO(p));
        return out;
    }

    @Override
    public List<RespuestaPagoDTO> buscarPorFiltros(String codigo, String estado) {
        List<Pago> pagos;
        boolean c = codigo != null && !codigo.isBlank();
        boolean e = estado != null && !estado.isBlank();
        if (c && e) {
            pagos = pagoDAO.findByCodigoAndEstado("%" + codigo.trim() + "%",
                    EstadoTransaccion.valueOf(estado));
        } else if (c) {
            pagos = pagoDAO.findByCodigoTransaccionLike("%" + codigo.trim() + "%");
        } else if (e) {
            pagos = pagoDAO.findByEstado(EstadoTransaccion.valueOf(estado));
        } else {
            pagos = pagoDAO.findAll();
        }
        List<RespuestaPagoDTO> out = new ArrayList<>();
        for (Pago p : pagos) out.add(aDTO(p));
        return out;
    }

    // ---------- Amortización (informativa) ----------

    @Override
    public List<CuotaDTO> calcularTablaAmortizacion(BigDecimal monto, int n, BigDecimal tasaMensual) {
        List<CuotaDTO> tabla = new ArrayList<>();
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0
                || n <= 1 || tasaMensual == null || tasaMensual.compareTo(BigDecimal.ZERO) <= 0) {
            return tabla;
        }

        BigDecimal i = tasaMensual; // 0.0134 para 1.34% mensual, por ejemplo
        BigDecimal unoMasI = BigDecimal.ONE.add(i, MC);
        BigDecimal potPos = unoMasI.pow(n, MC);
        BigDecimal potNeg = BigDecimal.ONE.divide(potPos, MC); // (1+i)^(-n)
        BigDecimal denom = BigDecimal.ONE.subtract(potNeg, MC);

        BigDecimal cuota = monto.multiply(i, MC).divide(denom, MC)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal saldo = monto;

        for (int k = 1; k <= n; k++) {
            BigDecimal intereses = saldo.multiply(i, MC).setScale(2, RoundingMode.HALF_UP);
            BigDecimal abono = cuota.subtract(intereses).setScale(2, RoundingMode.HALF_UP);
            BigDecimal nuevoSaldo = saldo.subtract(abono, MC);

            // Ajuste final para eliminar residuo por redondeo
            if (k == n) {
                abono = saldo.setScale(2, RoundingMode.HALF_UP);
                BigDecimal cuotaAjustada = abono.add(intereses).setScale(2, RoundingMode.HALF_UP);
                cuota = cuotaAjustada;
                nuevoSaldo = BigDecimal.ZERO;
            }

            CuotaDTO fila = new CuotaDTO();
            fila.setNumero(k);
            fila.setValorAPagar(cuota);
            fila.setIntereses(intereses);
            fila.setAbonoCapital(abono);
            fila.setSaldo(nuevoSaldo.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP));

            tabla.add(fila);
            saldo = nuevoSaldo;
        }
        return tabla;
    }

    // ---------- Helpers privados ----------

    private RespuestaPagoDTO aDTO(Pago p) {
        RespuestaPagoDTO dto = mapper.map(p, RespuestaPagoDTO.class);
        dto.setNombreCompletoCliente(p.getCliente().getNombres() + " " + p.getCliente().getApellidos());
        dto.setNumeroTarjetaEnmascarado(enmascararTarjeta(p.getTarjeta().getNumero()));
        dto.setComercio(p.getComercio());
        // dto.setTablaAmortizacion(...) -> solo si quieres precalcular cuando n>1
        return dto;
    }

    private String enmascararTarjeta(String numero) {
        if (numero == null || numero.length() < 10) return numero;
        return numero.substring(0, 6) + "****" + numero.substring(numero.length() - 4);
    }

    private String generarCodigoTransaccion() {
        String alfanum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 12; i++) sb.append(alfanum.charAt(rnd.nextInt(alfanum.length())));
        return sb.toString();
    }

    // Luhn simple
    private boolean validaLuhn(String numero) {
        if (numero == null || numero.isBlank()) return false;
        int sum = 0; boolean alt = false;
        for (int i = numero.length() - 1; i >= 0; i--) {
            char ch = numero.charAt(i);
            if (ch < '0' || ch > '9') return false;
            int n = ch - '0';
            if (alt) { n *= 2; if (n > 9) n -= 9; }
            sum += n; alt = !alt;
        }
        return sum % 10 == 0;
    }
}