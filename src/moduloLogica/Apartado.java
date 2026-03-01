package moduloLogica;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * requisitos RF-04 y RF-06
 * @author Danna Padilla
 */
public class Apartado {
    private int idApartado;
    private int idCliente;
    private int idProducto;
    private int idUsuario;
    private Timestamp fechaInicio;
    private Timestamp fechaLimite;
    private double totalPrenda;
    private double pagoInicial;
    private String estado; //ENUM
    
    public Apartado(){
        this.fechaInicio = new Timestamp(System.currentTimeMillis());
        //30 dias
        this.fechaLimite = calcularVencimiento(this.fechaInicio);
        this.estado = "Pendiente"; //default
 
    }
    
    //funciones
    private Timestamp calcularVencimiento (Timestamp inicio){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(inicio.getTime());
        cal.add(Calendar.DAY_OF_MONTH, 30); 
        return new Timestamp(cal.getTimeInMillis());
    }
    
    //Getter y setters 
    
    public int getIdApartado(){return idApartado;}
    public void setIdApartado(int idApartado){ this.idApartado = idApartado;}
    
    public int getIdCliente(){return idCliente;}
    public void setIdCliente(int idCliente){this.idCliente = idCliente;}
    
    public int getIdProducto (){return idProducto;}
    public void setIdProducto(int idProducto){this.idProducto = idProducto;}
    
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    
    public Timestamp getFechaInicio(){return fechaInicio;}
    public void setFechaInicio(Timestamp fechaInicio){this.fechaInicio = fechaInicio;}
    
    public Timestamp getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(Timestamp fechaLimite) { this.fechaLimite = fechaLimite; }
    
    public double getTotalPrenda() { return totalPrenda; }
    public void setTotalPrenda(double totalPrenda) { this.totalPrenda = totalPrenda; }

    public double getPagoInicial() { return pagoInicial; }
    public void setPagoInicial(double pagoInicial) { this.pagoInicial = pagoInicial; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public double getSaldoPendiente(){return this.totalPrenda - this.pagoInicial;}
    
}


