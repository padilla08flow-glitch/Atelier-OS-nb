package moduloLogica;

/**
 * Refactorizacion con Herencia
 * @author Danna Padilla
 */
public class Design extends Articulo{
    private String material;
    private String tecnica;
    private String color;
    private String rutaImagen;
    
    //CONSTRUCTOR VACIO
    public Design(){
    super();
    }
    
    //CONSTRUCTOR
    public Design(int id, String nombre, double precio, String material, String tecnica, String color, String rutaImagen){
        super(id, nombre, precio);
        this.material = material;
        this.tecnica = tecnica;
        this.color = color;
        this.rutaImagen = rutaImagen;
    }
    
    //GETTERS Y SETTERS
    public int getIdDesign() { return getId(); }
    public void setIdDesign(int idDesign) { setId(idDesign); }
    
    public String getNombreModelo() { return getNombre(); }
    public void setNombreModelo(String nombreModelo) { setNombre(nombreModelo); }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getTecnica() { return tecnica; }
    public void setTecnica(String tecnica) { this.tecnica = tecnica; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    //modificacion sugerida
    //acceso a imagenes
    
    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }
    
    
    @Override
    public String toString() {
        String infoExtra = (color != null && !color.isEmpty()) ? " (" + color + ")" : "";
        return getNombre() != null ? getNombre() + infoExtra : "Diseño sin nombre";
    }
}
