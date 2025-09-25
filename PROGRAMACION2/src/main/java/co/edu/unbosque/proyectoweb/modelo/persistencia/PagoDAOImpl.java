package co.edu.unbosque.proyectoweb.modelo.persistencia;

import co.edu.unbosque.proyectoweb.modelo.EstadoTransaccion;
import co.edu.unbosque.proyectoweb.modelo.Pago;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.util.List;

@Stateless
public class PagoDAOImpl implements PagoDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Pago save(Pago p) {
        if (p.getId() == null) {
            em.persist(p);
            return p;
        } else {
            return em.merge(p);
        }
    }

    @Override
    public Pago findById(Long id) {
        return em.find(Pago.class, id);
    }

    @Override
    public List<Pago> findAll() {
        return em.createNamedQuery("Pago.findAll", Pago.class).getResultList();
    }

    @Override
    public List<Pago> findByCodigoTransaccionLike(String codigoLike) {
        return em.createNamedQuery("Pago.findByCodigoTransaccionLike", Pago.class)
                .setParameter("codigo", codigoLike)
                .getResultList();
    }

    @Override
    public List<Pago> findByEstado(EstadoTransaccion estado) {
        return em.createNamedQuery("Pago.findByEstado", Pago.class)
                .setParameter("estado", estado)
                .getResultList();
    }

    @Override
    public List<Pago> findByCodigoAndEstado(String codigoLike, EstadoTransaccion estado) {
        return em.createNamedQuery("Pago.findByCodigoAndEstado", Pago.class)
                .setParameter("codigo", codigoLike)
                .setParameter("estado", estado)
                .getResultList();
    }

    @Override
    public long countByCliente(Long clienteId) {
        return em.createNamedQuery("Pago.countByCliente", Long.class)
                .setParameter("clienteId", clienteId)
                .getSingleResult();
    }
}
