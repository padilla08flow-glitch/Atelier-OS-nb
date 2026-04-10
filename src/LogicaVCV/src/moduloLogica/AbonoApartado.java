package moduloLogica;
import java.sql.Timestamp;

/**
 *
 * @author Danna Padilla
 */
public class AbonoApartado {
    
    private int idAbono;
    private int idApartado;
    private double montoAbono;
    private Timestamp fechaAbono;

    public AbonoApartado() {}
    
    //getter y setter
    public int getIdAbono(){return idAbono;}
    public void setIdAbono(int idAbono){ this.idAbono = idAbono;}
    
    public int getIdApartado(){return idApartado;}
    public void setIdApartado(int idApartado){this.idApartado = idApartado;}
    
    public double getMontoAbono(){return montoAbono;}
    public void setMontoAbono(double montoAbono){this.montoAbono = montoAbono;}
    
    public Timestamp getFechaAbono(){return fechaAbono;}
    public void setFechaAbono(Timestamp fechaAbono){this.fechaAbono = fechaAbono;}
    
    
}
