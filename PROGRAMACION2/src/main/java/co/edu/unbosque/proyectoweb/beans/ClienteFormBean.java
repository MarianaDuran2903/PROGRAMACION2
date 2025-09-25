package co.edu.unbosque.proyectoweb.beans;

import co.edu.unbosque.proyectoweb.modelo.persistencia.ClienteDTO;
import co.edu.unbosque.proyectoweb.modelo.servicios.IServicioCliente;

import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Named("clienteFormBean")           
@ViewScoped
public class ClienteFormBean implements Serializable {

    private ClienteDTO cliente = new ClienteDTO();

    @Inject
    private IServicioCliente servicioCliente;

    public String guardar() {
        try {
            servicioCliente.crear(cliente);
            return "index?faces-redirect=true";
        } catch (Exception e) {
            return null; 
        }
    }

    public ClienteDTO getCliente() { return cliente; }
    public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }
}