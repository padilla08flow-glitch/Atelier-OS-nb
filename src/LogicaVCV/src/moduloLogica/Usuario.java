package moduloLogica;

/**
 *
 * @author Danna Padilla
 */
public class Usuario {
    private int id;
    private String nombreUsuario;
    private String rol;
    
    public Usuario() {}
    
    public Usuario(int id, String nombreUsuario) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
