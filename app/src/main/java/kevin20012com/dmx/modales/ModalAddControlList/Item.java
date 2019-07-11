package kevin20012com.dmx.modales.ModalAddControlList;

public class Item {

    private int imgFoto;
    private String Titulo;
    private String Descripcion;

    public Item(int img, String tit, String cont){
        imgFoto = img;
        Titulo = tit;
        Descripcion = cont;
    }

    public int getImgFoto() { return imgFoto;}

    public String getTitulo() { return Titulo;}

    public String getDescripcion() { return Descripcion;}
}
