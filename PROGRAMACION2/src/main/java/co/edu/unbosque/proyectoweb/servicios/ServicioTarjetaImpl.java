package co.edu.unbosque.proyectoweb.servicios;

import co.edu.unbosque.proyectoweb.modelo.Cliente;
import co.edu.unbosque.proyectoweb.modelo.TarjetaCredito;
import co.edu.unbosque.proyectoweb.modelo.excepciones.EntidadNoEncontradaException;
import co.edu.unbosque.proyectoweb.modelo.excepciones.ExcepcionNegocio;
import co.edu.unbosque.proyectoweb.modelo.persistencia.ClienteDAO;
import co.edu.unbosque.proyectoweb.modelo.persistencia.TarjetaCreditoDAO;
import co.edu.unbosque.proyectoweb.modelo.persistencia.TarjetaDTO;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ServicioTarjetaImpl implements IServicioTarjeta {

    @Inject
    private TarjetaCreditoDAO tarjetaDAO;
    @Inject private ClienteDAO clienteDAO;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public TarjetaDTO registrar(TarjetaDTO dto) throws ExcepcionNegocio, EntidadNoEncontradaException {
        if (dto == null) throw new ExcepcionNegocio("Datos de tarjeta vacíos.");
        if (dto.getIdentificacionCliente() == null || dto.getIdentificacionCliente().isBlank())
            throw new ExcepcionNegocio("Debe indicar la identificación del cliente.");
        if (dto.getNumero() == null || dto.getNumero().isBlank())
            throw new ExcepcionNegocio("El número de tarjeta es obligatorio.");
        if (dto.getMarca() == null || dto.getMarca().isBlank())
            throw new ExcepcionNegocio("La marca de la tarjeta es obligatoria.");
        if (dto.getFechaExpiracion() == null || dto.getFechaExpiracion().isBefore(LocalDate.now().withDayOfMonth(1)))
            throw new ExcepcionNegocio("La fecha de expiración no puede estar vencida.");

        // Cliente dueño
        Cliente cliente = clienteDAO.findByIdentificacion(dto.getIdentificacionCliente());
        if (cliente == null) throw new EntidadNoEncontradaException("Cliente no encontrado.");

        // ¿Existe ese número ya?
        TarjetaCredito repetida = tarjetaDAO.findByNumero(dto.getNumero());
        if (repetida != null) throw new ExcepcionNegocio("Ya existe una tarjeta con ese número.");

        // (opcional) Luhn
        if (!validaLuhn(dto.getNumero()))
            throw new ExcepcionNegocio("El número de tarjeta no es válido.");

        TarjetaCredito entidad = mapper.map(dto, TarjetaCredito.class);
        entidad.setCliente(cliente);
        if (entidad.getActiva() == null) entidad.setActiva(Boolean.TRUE);

        entidad = tarjetaDAO.save(entidad);

        // Mapear de vuelta (si quieres devolver enmascarado, puedes ajustar aquí)
        TarjetaDTO salida = mapper.map(entidad, TarjetaDTO.class);
        return salida;
    }

    @Override
    public TarjetaDTO obtenerPorId(Long id) throws EntidadNoEncontradaException {
        TarjetaCredito t = tarjetaDAO.findById(id);
        if (t == null) throw new EntidadNoEncontradaException("Tarjeta no encontrada.");
        return mapper.map(t, TarjetaDTO.class);
    }

    @Override
    public TarjetaDTO obtenerPorNumero(String numero) {
        TarjetaCredito t = tarjetaDAO.findByNumero(numero);
        return (t == null) ? null : mapper.map(t, TarjetaDTO.class);
    }

    @Override
    public List<TarjetaDTO> listarActivasPorCliente(Long clienteId) {
        List<TarjetaCredito> lista = tarjetaDAO.findActivasByCliente(clienteId);
        List<TarjetaDTO> out = new ArrayList<>();
        for (TarjetaCredito t : lista) out.add(mapper.map(t, TarjetaDTO.class));
        return out;
    }

    @Override
    public TarjetaDTO desactivar(Long idTarjeta) throws EntidadNoEncontradaException {
        TarjetaCredito t = tarjetaDAO.findById(idTarjeta);
        if (t == null) throw new EntidadNoEncontradaException("Tarjeta no encontrada.");
        t.setActiva(false);
        t = tarjetaDAO.save(t);
        return mapper.map(t, TarjetaDTO.class);
    }

    // ------- utilitario Luhn (igual al del servicio de pagos) ------- esto lo hice por chtgpt
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
