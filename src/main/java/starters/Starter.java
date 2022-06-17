package starters;

import analysers.Analyser;
import interfaces.Interface;
import languages.Language;
import parsers.Reader;
import parsers.Writer;

public class Starter{

    public static void main(String[] args){
        Interface app = new Interface();
        //
        /*Reader stopRU = new Reader("D:\\_Mehi\\6sem\\java\\analyser\\src\\main\\resources\\stopRU.txt");
        stopRU.read();
        Language russian = new Language("ru");
        russian.generateStopSymbols(stopRU);
        //
        Reader stopPM = new Reader("D:\\_Mehi\\6sem\\java\\analyser\\src\\main\\resources\\stopPM.txt");
        stopPM.read();
        Language punctuationMark = new Language("pm");
        punctuationMark.generateStopSymbols(stopPM);
        //
        Reader textReader = new Reader("D:\\_Mehi\\6sem\\java\\analyser\\src\\main\\resources\\text.txt");
        textReader.read();
        //
        Analyser a = new Analyser();
        a.generateText(textReader);
        a.analyse();
        a.sortFrequenciesByValue();
        //
        Writer textWriter = new Writer("D:\\_Mehi\\6sem\\java\\analyser\\src\\main\\resources\\out\\text.txt", a);
        textWriter.saveText();
        Writer frequenciesWriter = new Writer("D:\\_Mehi\\6sem\\java\\analyser\\src\\main\\resources\\out\\frequencies.xlsx", a);
        frequenciesWriter.saveFrequencies();*/
    }

}
