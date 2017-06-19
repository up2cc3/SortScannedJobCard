

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class SortScannedJobCards {


    private static Rectangle areajc = new Rectangle(
            2060, 20, 300, 300);
    private static Rectangle areamf = new Rectangle(
            260, 850, 300, 200);
    private static Rectangle areac = new Rectangle(
            1250, 525, 300, 100);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        String carpetaFinalLocal = "C:\\Users\\ccc\\Documents\\Tesseract\\Sorted\\";
        String carpetaFinal = "S:\\Production\\Logistics\\DISPATCHES\\Dispatched - 2017\\";
        String result = null;
        File imageFile = null;
        PDFDocument pdf = null;
        Ficheros ficheros = new Ficheros(result, imageFile, pdf);

        java.util.List<Ficheros> listaFicheros = ficheros.buscarFicheros();
        Tesseract instance = new Tesseract(); // JNA Interface Mapping
        // Tesseract1 instance = new Tesseract1(); // JNA Direct Mapping
        instance.setDatapath(".");
        for (Ficheros f : listaFicheros)
            try {
                String resultado = instance.doOCR(f.getImageFile(), areajc);
                String resultadoc = instance.doOCR(f.getImageFile(), areac);
                String resultadomf = instance.doOCR(f.getImageFile(), areamf);
                System.out.println(resultado + " resultado");
                System.out.println(resultadoc + " resultadoc");
                System.out.println(resultadomf + " resultadomf");

                if ((resultado.substring(0).startsWith("3"))||(resultado.substring(0).startsWith("4") )) {
                    f.setResult(resultado.substring(0, 5));
                } else if ((resultado.substring(0).startsWith("C"))) {
                    f.setResult(resultado.substring(0, 6));
                }else if ((resultadoc.substring(0).startsWith("C"))) {
                    f.setResult(resultadoc.substring(0, 6));
                }else if ((resultadoc.substring(0).startsWith("3"))||(resultado.substring(0).startsWith("4") )) {
                    f.setResult(resultadoc.substring(0, 5));
                }else if ((resultadomf.substring(0).startsWith("C"))) {
                    f.setResult(resultadomf.substring(0, 6));
                }else if ((resultadomf.substring(0).startsWith("3"))||(resultado.substring(0).startsWith("4") )) {
                    f.setResult(resultadomf.substring(0, 5));
                }

                if (f.getResult() != null) {


                    File newfile = new File(carpetaFinal + f.getResult() + ".pdf");
                    if (newfile.exists()) {
                        PDFDocument newpdf = new PDFDocument();
                        newpdf.load(new File(carpetaFinal + f.getResult() + ".pdf"));
                        PDFDocument myPdf = new PDFDocument();
                        myPdf.load(f.getImageFile());
                        SafeAppenderModifier modifier = new SafeAppenderModifier();
                        Map<String, Serializable> parameters = new HashMap<String, Serializable>();
                        parameters.put(SafeAppenderModifier.PARAMETER_APPEND_DOCUMENT, myPdf);
                        Document apennd = modifier.modify(newpdf, parameters);
                        apennd.write(new File(carpetaFinal + f.getResult() + ".pdf"));
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