package parsers;

import analysers.Analyser;

public abstract class Writer{

    private String path;
    private Analyser analyser;

    public Writer(String path, Analyser analyser) {
        this.path = path;
        this.analyser = analyser;
    }

    public String getPath() {
        return path;
    }

    public Analyser getAnalyser() {
        return analyser;
    }

    abstract public void save();

}

