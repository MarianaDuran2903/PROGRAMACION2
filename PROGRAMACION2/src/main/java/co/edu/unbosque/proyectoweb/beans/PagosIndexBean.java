package co.edu.unbosque.proyectoweb.beans;


import co.edu.unbosque.proyectoweb.modelo.persistencia.RespuestaPagoDTO;
import co.edu.unbosque.proyectoweb.servicios.IServicioPago;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("pagosIndexBean") // con respecto al pom, debemos usar las peristencias que nos enseño el profe y no mas!!
@ViewScoped
public class PagosIndexBean implements Serializable {

    @Inject
    private IServicioPago servicioPago;

    private List<RespuestaPagoDTO> pagos;

    @PostConstruct
    public void init() {
        pagos = servicioPago.listarPagos();// Carga inicial desde BD, se supone q el bean solo toca el servicio, no directamente al dao
        //un bean puede llamar otro bean se es para recargar la vista
    }

    public List<RespuestaPagoDTO> getPagos() {
        return pagos;
    }
}

