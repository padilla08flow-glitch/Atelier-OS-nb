package moduloLogica;

/**
 *
 * @author Danna Padilla
 */
public abstract class Articulo {
    protected int id;
    protected String nombre;
    protected double precio;
    
    public Articulo(){}
    
    public Articulo(int id, String nombre, double precio){
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }
    
    public int getId() {return id;}
    public String getNombre(){ return nombre;}
    public double getPrecio(){ return precio;}
    
    public void setId(int id){this.id = id;}
    public void setNombre(String nombre){ this.nombre = nombre;}
    public void setPrecio (double precio) { this.precio = this.precio;}
    
}
