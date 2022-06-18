package parsers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Reader{
    // for reading an original text and stop words

    private ArrayList<String> symbols; // original version
    private final String path;

    public ArrayList<String> getText() {
        return symbols;
    }

    public Reader(String path) {
        this.path = path;
        this.symbols = new ArrayList<>();
    }

    public void read(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            StringBuilder symbol = new StringBuilder();
            boolean prevCharIsValuable = true;
            while(reader.ready()){
                int currentCharacterCode = reader.read();
                char currentCharacter = (char) currentCharacterCode;
                boolean russian = currentCharacter >= 1040 && currentCharacter <= 1103;
                boolean english = currentCharacter >= 65 && currentCharacter <= 90 || currentCharacter >= 97 && currentCharacter <= 122;
                boolean number = currentCharacter >= 48 && currentCharacter <= 57;
                if(russian || english || number){
                    if(prevCharIsValuable) symbol.append(currentCharacter);
                    else{
                        symbols.add(symbol.toString());
                        symbol.delete(0, symbol.length());
                        symbol.append(currentCharacter);
                    }
                    prevCharIsValuable = true;
                } else{
                    symbols.add(symbol.toString());
                    symbol.delete(0, symbol.length());
                    symbol.append(currentCharacter);
                    prevCharIsValuable = false;
                }
            }
            symbols.add(symbol.toString());
            reader.close();
        } catch(IOException e){
            System.out.println("Cannot read the file due to: "+e.getMessage());
        }
    }

}

