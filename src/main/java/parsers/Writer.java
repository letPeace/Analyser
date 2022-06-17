package parsers;

import analysers.Analyser;
import analysers.SymbolData;
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
import java.util.Map.Entry;

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

    public void saveText(){ // to TXT
        try{
            saveText(analyser.getText());
        } catch(IOException e){
            System.out.println("Failed saving text: "+e.getMessage());
        }
    }

    private void saveText(ArrayList<SymbolData> symbolsData) throws IOException{
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
        for(SymbolData symbolData : symbolsData) writer.write(symbolData.getSymbol());
        writer.close();
    }

    // FREQUENCIES

    public void saveFrequencies(){ // to EXCEL
        try{
            saveFrequencies(analyser.getFrequencies());
        } catch(IOException e){
            System.out.println("Failed saving frequencies: "+e.getMessage());
        }
    }

    private void saveFrequencies(LinkedHashMap<SymbolData, Integer> frequencies) throws IOException{

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("frequencies");

        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("WORD");
        header.createCell(1).setCellValue("LANG");
        header.createCell(2).setCellValue("FREQUENCY");
        int i = 1;
        for(Entry entry : frequencies.entrySet()){
            SymbolData symbolData = (SymbolData) entry.getKey();
            XSSFRow rw = sheet.createRow(i);
            rw.createCell(0).setCellValue(symbolData.getSymbol());
            rw.createCell(1).setCellValue(symbolData.getLang().getCode());
            rw.createCell(2).setCellValue(entry.getValue().toString());
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
