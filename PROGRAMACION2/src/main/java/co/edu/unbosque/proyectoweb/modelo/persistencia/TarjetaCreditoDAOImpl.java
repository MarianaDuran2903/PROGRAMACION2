package co.edu.unbosque.proyectoweb.modelo.persistencia;

import co.edu.unbosque.proyectoweb.modelo.TarjetaCredito;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.*;

import java.util.List;

@Stateless
public class TarjetaCreditoDAOImpl implements TarjetaCreditoDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public TarjetaCredito save(TarjetaCredito t) {
        if (t.getId() == null) {
            em.persist(t);
            return t;
        } else {
            return em.merge(t);
        }
    }

    @Override
    public TarjetaCredito findById(Long id) {
        return em.find(TarjetaCredito.class, id);
    }

    @Override
    public TarjetaCredito findByNumero(String numero) {
        List<TarjetaCredito> ls = em.createNamedQuery("TarjetaCredito.findByNumero", TarjetaCredito.class)
                .setParameter("numero", numero)
                .getResultList();
        return ls.isEmpty() ? null : ls.get(0);
    }

    @Override
    public List<TarjetaCredito> findActivasByCliente(Long clienteId) {
        return em.createNamedQuery("TarjetaCredito.findActivasByCliente", TarjetaCredito.class)
                .setParameter("clienteId", clienteId)
                .getResultList();
    }
}
