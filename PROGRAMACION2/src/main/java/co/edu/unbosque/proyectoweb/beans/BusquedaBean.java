package co.edu.unbosque.proyectoweb.beans;

import co.edu.unbosque.proyectoweb.modelo.persistencia.CuotaDTO;
import co.edu.unbosque.proyectoweb.modelo.persistencia.RespuestaPagoDTO;
import co.edu.unbosque.proyectoweb.servicios.IServicioPago;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Named("busquedaBean")
@ViewScoped
public class BusquedaBean implements Serializable {

    @Inject
    private IServicioPago servicioPago;

    // Filtros
    private String codigoTransaccion;
    private String estadoTransaccion;

    // Datos de la tabla
    private List<RespuestaPagoDTO> pagos = new ArrayList<>();

    // --- Estado para diálogos ---
    // Cuotas
    private BigDecimal valorPagadoCuotas;
    private Integer numeroCuotasSeleccionadas;
    private List<CuotaDTO> tablaCuotas = java.util.Collections.emptyList();

    // Comprobante
    private RespuestaPagoDTO pagoSeleccionado;

    @PostConstruct
    public void init() {
        pagos = servicioPago.listarPagos();
    }

    public void buscar() {
        boolean sinCodigo = codigoTransaccion == null || codigoTransaccion.isBlank();
        boolean sinEstado = estadoTransaccion == null || estadoTransaccion.isBlank();

        if (sinCodigo && sinEstado) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Debes ingresar al menos un filtro.", "Código o estado."));
            return;
        }
        pagos = servicioPago.buscarPorFiltros(
                sinCodigo ? null : codigoTransaccion.trim(),
                sinEstado ? null : estadoTransaccion
        );
        if (pagos.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Sin resultados","Prueba otros filtros."));
        }
    }

    public void limpiar() {
        codigoTransaccion = "";
        estadoTransaccion = "";
        pagos = servicioPago.listarPagos();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,"Filtros limpiados","Mostrando todo."));
    }

    // ---------- ACCIONES DE LA COLUMNA ----------

    public void abrirDialogoCuotas(Long idPago) {
        RespuestaPagoDTO pago = servicioPago.obtenerPagoPorId(idPago);
        if (pago == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Pago no encontrado",null));
            return;
        }
        if (pago.getNumeroCuotas() == null || pago.getNumeroCuotas() <= 1) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,"Este pago no tiene cuotas","Nº de cuotas <= 1"));
            tablaCuotas = java.util.Collections.emptyList();
            return;
        }

        valorPagadoCuotas = pago.getTotal();                 // o subtotal, según regla
        numeroCuotasSeleccionadas = pago.getNumeroCuotas();
        BigDecimal tasaMensual = new BigDecimal("0.0134");   // 1.34% mensual

        tablaCuotas = servicioPago.calcularTablaAmortizacion(
                valorPagadoCuotas, numeroCuotasSeleccionadas, tasaMensual);

        PrimeFaces.current().ajax().update("dlgCuotasForm");
        PrimeFaces.current().executeScript("PF('dlgCalcularCuotas').show();");
    }

    public void abrirDialogoComprobante(Long idPago) {
        pagoSeleccionado = servicioPago.obtenerPagoPorId(idPago);
        if (pagoSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Pago no encontrado",null));
            return;
        }
        PrimeFaces.current().ajax().update("dlgComprobanteForm");
        PrimeFaces.current().executeScript("PF('dlgComprobante').show();");
    }

    // ---------- Getters/Setters ----------
    public String getCodigoTransaccion() { return codigoTransaccion; }
    public void setCodigoTransaccion(String codigoTransaccion) { this.codigoTransaccion = codigoTransaccion; }
    public String getEstadoTransaccion() { return estadoTransaccion; }
    public void setEstadoTransaccion(String estadoTransaccion) { this.estadoTransaccion = estadoTransaccion; }
    public List<RespuestaPagoDTO> getPagos() { return pagos; }

    public BigDecimal getValorPagadoCuotas() { return valorPagadoCuotas; }
    public Integer getNumeroCuotasSeleccionadas() { return numeroCuotasSeleccionadas; }
    public List<CuotaDTO> getTablaCuotas() { return tablaCuotas; }

    public RespuestaPagoDTO getPagoSeleccionado() { return pagoSeleccionado; }
}