package moduloLogica;

/**
 * Refactorización para heredar de la clase Articulo
 * RNF-02 Mantenibilidad
 * @author Danna Padilla
 */
public class Producto extends Articulo{
    //ya solo lleva estos
    private int idDesign;
    private String sku;
    private String talla;
    private String genero;
    private int stock;
    private String rutaImagen;
    private String material;
    private String tecnica;
    private String color;
    
    public Producto() {
        super();
    }
    //constructor
    public Producto(int id, String nombre, double precio, String sku, String talla, int stock, String rutaImagen) {
        super(id, nombre, precio);
        this.sku = sku;
        this.talla = talla;
        this.stock = stock;
        this.rutaImagen = rutaImagen;
    }
    
    @Override
    public void setPrecio(double precio) {
        super.setPrecio(precio);
    }
    
     //getters - Setter
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getTalla() {return talla;}
    public void setTalla(String talla){this.talla = talla; }
    
    public int getIdDesign() { return idDesign; }
    public void setIdDesign(int idDesign) { this.idDesign = idDesign; }
    
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    
    //ahora es nombre
    public String getNombreModelo() { return getNombre(); }
    public void setNombreModelo(String nombreModelo) { setNombre(nombreModelo); }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getTecnica() { return tecnica; }
    public void setTecnica(String tecnica) { this.tecnica = tecnica; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }
    
    
}


