package parsers;

import analysers.Analyser;
import analysers.SymbolData;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class WriterFrequencies extends Writer{

    public WriterFrequencies(String path, Analyser analyser){
        super(path, analyser);
    }

    public void save(){ // to EXCEL
        try{
            save(getAnalyser().getFrequencies());
        } catch(IOException e){
            System.out.println("Failed saving frequencies: "+e.getMessage());
        }
    }

    private void save(LinkedHashMap<SymbolData, Integer> frequencies) throws IOException{

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("frequencies");

        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("WORD");
        header.createCell(1).setCellValue("LANG");
        header.createCell(2).setCellValue("FREQUENCY");
        int i = 1;
        for(Map.Entry entry : frequencies.entrySet()){
            SymbolData symbolData = (SymbolData) entry.getKey();
            XSSFRow rw = sheet.createRow(i);
            rw.createCell(0).setCellValue(symbolData.getSymbol());
            rw.createCell(1).setCellValue(symbolData.getLang().getCode());
            rw.createCell(2).setCellValue(entry.getValue().toString());
            i++;
        }

        try{
            workbook.write(new FileOutputStream(getPath()));
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
