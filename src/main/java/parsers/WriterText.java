package parsers;

import analysers.Analyser;
import analysers.SymbolData;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class WriterText extends Writer{

    public WriterText(String path, Analyser analyser){
        super(path, analyser);
    }

    public void save(){ // to TXT
        try{
            save(getAnalyser().getText());
        } catch(IOException e){
            System.out.println("Failed saving text: "+e.getMessage());
        }
    }

    private void save(ArrayList<SymbolData> symbolsData) throws IOException{
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getPath()), StandardCharsets.UTF_8));
        for(SymbolData symbolData : symbolsData) writer.write(symbolData.getSymbol());
        writer.close();
    }

}
