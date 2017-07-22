

/**
 * Created by ccc on 30/05/2017.
 */

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.log4j.BasicConfigurator;
import org.ghost4j.document.Document;
import org.ghost4j.document.DocumentException;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.modifier.ModifierException;
import org.ghost4j.modifier.SafeAppenderModifier;

import java.awt.*;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SortScannedJobCards {


    private static Rectangle areajc = new Rectangle(
            2060, 20, 300, 300);
    private static Rectangle areamf = new Rectangle(
            262, 900, 300, 100);
    private static Rectangle areac = new Rectangle(
            1250, 525, 300, 100);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        String carpetaFinalLocal = "C:\\Users\\ccc\\Documents\\Tesseract\\Sorted\\";
        String carpetanulaLocal = "C:\\Users\\ccc\\Documents\\Tesseract\\Unsorted\\null\\";
        String carpetaFinal = "S:\\Production\\Logistics\\DISPATCHES\\Dispatches - ";
        String carpetanula = "S:\\Production\\Logistics\\DISPATCHES\\unsorted\\notsorted\\";

        String result = null;
        File imageFile = null;
        PDFDocument pdf = null;
        Ficheros ficheros = new Ficheros(result, imageFile, pdf);
        vacio v = new vacio();
        Date today = Calendar.getInstance().getTime();
        String date = today.toString();
        String mes = date.substring(4, 7);
        String year = date.substring(25, 29);
        String dia = date.substring(8, 10);
        File dir = new File(carpetaFinalLocal + year + "\\" + mes + " " + year + "\\");
        try {
            dir.mkdirs();

        } catch (Exception e) {
        }

        java.util.List<Ficheros> listaFicheros = ficheros.buscarFicheros();
        Tesseract instance = new Tesseract(); // JNA Interface Mapping
        // Tesseract1 instance = new Tesseract1(); // JNA Direct Mapping
        instance.setDatapath(".");
        for (Ficheros f : listaFicheros)

            try {
                System.out.println("empezamos" + v.getResultado2() + "-" + v.isVacio() + "-");
                String resultado1 = instance.doOCR(f.getImageFile(), areajc);
                //String resultadoc = instance.doOCR(f.getImageFile(), areac);
                //String resultadomf = instance.doOCR(f.getImageFile(), areamf);
                System.out.println(resultado1 + " resultado");
                //System.out.println(resultadoc + " resultadoc");
                //System.out.println(resultadomf + " resultadomf");
                String resultado = resultado1.replaceAll("\\s", "");
                System.out.println(resultado + "- resultado");
                if (resultado.length() == 5 && v.isNumeric(resultado)) {
                    f.setResult(resultado);
                    v.setResultado2(resultado);
                    v.setVacio(true);

                } else if (resultado.substring(0).startsWith("C") && v.isNumeric(resultado.substring(1, 6)) && resultado.length() == 6) {
                    f.setResult(resultado);
                    v.setResultado2(resultado);
                    v.setVacio(true);

                } else if (v.isVacio() && f.getResult() == null) {
                    f.setResult(v.getResultado2());
                    v.setVacio(false);
                    File newfile = new File(dir + "\\" + dia + mes + year + " " + f.toString() + ".pdf");
                    File copyfile = f.getImageFile();
                    InputStream in = new FileInputStream(copyfile);
                    OutputStream out = new FileOutputStream(newfile);

                    try {
                        byte[] buf = new byte[1024];
                        int len;

                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        in.close();
                        out.close();
                        System.out.println("Rename al monton y pegado");
                    } catch (Exception e) {
                        System.out.println("copia failed");
                    }
                    
                } else if (f.getResult() == null) {
                    f.setResult(dia + mes + year + " " + f.toString());
                    System.out.println("Rename al monton" + f.getResult());
                }

                if (f.getResult() != null) {

                    File newfile = new File(dir + "\\" + f.getResult() + ".pdf");

                    if (newfile.exists()) {
                        PDFDocument newpdf = new PDFDocument();
                        newpdf.load(new File(dir + "\\" + f.getResult() + ".pdf"));
                        PDFDocument myPdf = new PDFDocument();
                        myPdf.load(f.getImageFile());
                        SafeAppenderModifier modifier = new SafeAppenderModifier();
                        Map<String, Serializable> parameters = new HashMap<String, Serializable>();
                        parameters.put(SafeAppenderModifier.PARAMETER_APPEND_DOCUMENT, myPdf);
                        Document apennd = modifier.modify(newpdf, parameters);
                        apennd.write(new File(dir + "\\" + f.getResult() + ".pdf"));
                        f.getImageFile().delete();

                    } else {

                        if (f.getImageFile().renameTo(newfile)) {
                            System.out.println("Rename succesful");
                        } else {
                            System.out.println("Rename failed");
                        }
                    }
                }

            } catch (TesseractException e) {
                System.err.println(e.getMessage());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (ModifierException e) {
                e.printStackTrace();
            }

    }

}