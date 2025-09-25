package co.edu.unbosque.proyectoweb.modelo.persistencia;

import co.edu.unbosque.proyectoweb.modelo.Cliente;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.util.List;

@Stateless
public class ClienteDAOImpl implements ClienteDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Cliente save(Cliente c) {
        if (c.getId() == null) {
            em.persist(c);
            return c;
        } else {
            return em.merge(c);
        }
    }

    @Override
    public Cliente findById(Long id) {
        return em.find(Cliente.class, id);
    }

    @Override
    public Cliente findByIdentificacion(String identificacion) {
        List<Cliente> list = em.createNamedQuery("Cliente.findByIdentificacion", Cliente.class)
                .setParameter("ident", identificacion)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Cliente> listar() {
        return em.createNamedQuery("Cliente.listar", Cliente.class).getResultList();
    }

    @Override
    @Transactional
    public Long insertar(ClienteDTO cliente) throws Exception {
        em.persist(cliente);
        em.flush();
        return cliente.getId();
    }
}
