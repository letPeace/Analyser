package parsers;

import analysers.Analyser;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Writer{

    private String path;
    private Analyser analyser;

    public Writer(String path, Analyser analyser) {
        this.path = path;
        this.analyser = analyser;
    }

    public void save(){
        saveText();
        saveFrequencies();
    }

    // TEXT

    public void saveText(){
        try{
            saveText(analyser.getText());
        } catch(IOException e){
            System.out.println(e);
        }
    }

    private void saveText(ArrayList<String> symbols) throws IOException{ // to TXT
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
        for(String symbol : symbols) writer.write(symbol);
        writer.close();
    }

    // FREQUENCIES

    public void saveFrequencies(){
        try{
            saveFrequencies(analyser.getFrequencies());
        } catch(IOException e){
            System.out.println(e);
        }
    }

    private void saveFrequencies(LinkedHashMap<String, Integer> frequencies) throws IOException{ // to EXCEL

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("frequencies");

        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("WORD");
        header.createCell(1).setCellValue("FREQUENCY");
        int i = 1;
        for(Map.Entry entry : frequencies.entrySet()){
            XSSFRow rw = sheet.createRow(i);
            rw.createCell(0).setCellValue(entry.getKey().toString());
            rw.createCell(1).setCellValue(entry.getValue().toString());
            i++;
        }

        try{
            workbook.write(new FileOutputStream(path));
        } catch(IOException e){
            throw new IOException("Failed writing data to file: "+e.getMessage());
        }

        try{
            workbook.close();
        } catch(IOException e){
            throw new IOException("Failed closing connection with workbook: "+e.getMessage());
        }

    }

}
