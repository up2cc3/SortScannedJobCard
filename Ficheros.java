

/**
 * Created by ccc on 30/05/2017.
 */
import org.ghost4j.document.PDFDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccc on 27/05/2017.
 */
public class Ficheros {

    private String result;
    private File imageFile;
    private PDFDocument pdf;
    public String carpetaInicialLocal = "C:\\Users\\ccc\\Documents\\Tesseract\\Unsorted\\";
    public String carpetaInicial = "S:\\Production\\Logistics\\DISPATCHES\\unsorted\\";


    public Ficheros(String result, File imageFile, PDFDocument pdf) {

        setResult(result);
        setImageFile(imageFile);
        setPdf(pdf);

    }

    public List<Ficheros>buscarFicheros() {
        List<Ficheros> ficheros = new ArrayList<Ficheros>();
        PDFDocument pdf=new PDFDocument();

        File fDirectorio = new File(carpetaInicial);
        if (fDirectorio.isDirectory()) {
            File[] myFiles = fDirectorio.listFiles();

            for (File myFile : myFiles) {

                if (myFile.getName().contains(".pdf")) {
                    try{
                        pdf.load(myFile);    } catch (Exception e) {
                        System.out.println("ERROR: " + e.getMessage());
                    }
                    ficheros.add(new Ficheros(null, myFile,pdf));

                }
            }
        }
        return ficheros;
    }




    public void setResult(String result) {
        this.result = result;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public PDFDocument getPdf() {
        return pdf;
    }

    public void setPdf(PDFDocument pdf) {
        this.pdf = pdf;
    }

    public String getResult() {
        return result;
    }

}

