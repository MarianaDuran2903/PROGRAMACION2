package co.edu.unbosque.proyectoweb.servicios;

import co.edu.unbosque.proyectoweb.modelo.Cliente;
import co.edu.unbosque.proyectoweb.modelo.excepciones.EntidadNoEncontradaException;
import co.edu.unbosque.proyectoweb.modelo.excepciones.ExcepcionNegocio;
import co.edu.unbosque.proyectoweb.modelo.persistencia.ClienteDAO;
import co.edu.unbosque.proyectoweb.modelo.persistencia.ClienteDTO;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Stateless // en las implementaciones de interface, solo nos ha enseñado el stateless!!
public class IServicioClienteImpl implements IservicioCliente {

    @Inject
    private ClienteDAO clienteDAO;
    private final ModelMapper mapper = new ModelMapper(); // nos enseño el datamaper,
//en resourses se añade el directorio META-INF y se pone el persistence.xml
    // Tambien se ponen las excepciones propias !!
    @Override
    public ClienteDTO registrar(ClienteDTO dto) throws ExcepcionNegocio {
        if (dto == null) throw new ExcepcionNegocio("Datos de cliente vacíos.");
        if (dto.getIdentificacion() == null || dto.getIdentificacion().isBlank())
            throw new ExcepcionNegocio("La identificación es obligatoria.");
        if (dto.getNombres() == null || dto.getNombres().isBlank()
                || dto.getApellidos() == null || dto.getApellidos().isBlank())
            throw new ExcepcionNegocio("Nombres y apellidos son obligatorios.");

        // ¿Existe ya por identificación?
        Cliente existente = clienteDAO.findByIdentificacion(dto.getIdentificacion());
        if (existente != null) {
            throw new ExcepcionNegocio("Ya existe un cliente con esa identificación.");
        }

        Cliente entidad = mapper.map(dto, Cliente.class);
        entidad = clienteDAO.save(entidad);
        return mapper.map(entidad, ClienteDTO.class);
    }

    @Override
    public ClienteDTO obtenerPorId(Long id) throws EntidadNoEncontradaException {
        Cliente c = clienteDAO.findById(id);
        if (c == null) throw new EntidadNoEncontradaException("Cliente no encontrado.");
        return mapper.map(c, ClienteDTO.class);
    }

    @Override
    public ClienteDTO obtenerPorIdentificacion(String identificacion) {
        Cliente c = clienteDAO.findByIdentificacion(identificacion);
        return (c == null) ? null : mapper.map(c, ClienteDTO.class);
    }

    @Override
    public List<ClienteDTO> listar() {
        List<Cliente> entidades = clienteDAO.listar();
        List<ClienteDTO> salida = new ArrayList<>();
        for (Cliente c : entidades) salida.add(mapper.map(c, ClienteDTO.class));
        return salida;
    }

    @Override
    public Long crear(ClienteDTO cliente) throws Exception {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente requerido");
        }
        if (cliente.getIdentificacion() == null || cliente.getIdentificacion().isBlank()) {
            throw new IllegalArgumentException("Identificación requerida");
        }
        return clienteDAO.insertar(cliente);
    }
}