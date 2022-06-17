package interfaces;

import analysers.Analyser;
import languages.Lang;
import languages.Language;
import parsers.Reader;
import parsers.Writer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

public class Interface extends JFrame{

    private Font font;
    private JMenuBar menuBar;
    private JMenu file;
    private JMenuItem uploadText;
    private JMenuItem analyse;
    private JMenuItem download;
    private JMenuItem exit;
    private JMenu settings;
    private JMenuItem uploadStoplist;
    private JMenu extraSettings;
    private JMenuItem languages;
    private JMenuItem stoplist;
    //
    private Reader textReader;
    private Reader stopSymbolsReader;
    private Analyser analyser;

    public Interface(){
        super("Analyse");
        init();
    }

    public void init(){
        // FILE
        file = new JMenu("File");
        uploadText = new JMenuItem("Upload"); // read original file
        analyse = new JMenuItem("Analyse"); // calculate frequencies
        download = new JMenuItem("Download"); // download results of analyse and editing text
        exit = new JMenuItem("Exit");

        file.add(uploadText);
        file.add(analyse);
        file.add(download);
        file.addSeparator();
        file.add(exit);

        // SETTINGS
        settings = new JMenu("Settings");
        uploadStoplist = new JMenuItem("Upload stoplist");
        extraSettings = new JMenu("Extra");
        languages = new JMenuItem("Choose languages");
        stoplist = new JMenuItem("Info about stoplist");

        settings.add(uploadStoplist);
        settings.add(extraSettings);
        extraSettings.add(languages);
        extraSettings.add(stoplist);

        // MENU
        menuBar = new JMenuBar();

        menuBar.add(file);
        menuBar.add(settings);

        //
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setJMenuBar(menuBar);
        this.setPreferredSize(new Dimension(500, 500));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        bindActionListeners();
        setDefaultFont();
    }

    public void bindActionListeners(){
        uploadText.addActionListener(new UploadText());
        analyse.addActionListener(new Analyse());
        download.addActionListener(new Download());
        exit.addActionListener(new Exit());

        uploadStoplist.addActionListener(new UploadStoplist());
        languages.addActionListener(new LanguagesList());
        stoplist.addActionListener(new UploadText());
    }

    public void setDefaultFont(){
        font = new Font("Verdana", Font.PLAIN, 12);

        file.setFont(font);
        uploadText.setFont(font);
        analyse.setFont(font);
        download.setFont(font);
        exit.setFont(font);
        settings.setFont(font);
        uploadStoplist.setFont(font);
        extraSettings.setFont(font);
        languages.setFont(font);
        stoplist.setFont(font);
    }

    class UploadText implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                String path = "";
                JFileChooser fileChooser = new JFileChooser("D:\\_Mehi\\6sem\\java\\analyser\\src\\main\\resources");
                int ret = fileChooser.showDialog(null, "Выбрать файл");
                if(ret == JFileChooser.APPROVE_OPTION) path = fileChooser.getSelectedFile().getAbsolutePath();
                if(path.equals("")) throw new FileNotFoundException("Выберите файл");
                textReader = new Reader(path);
                textReader.read();
                analyser = new Analyser();
                analyser.generateText(textReader);
                String message = "Данные импортированы из "+path.substring(path.lastIndexOf('\\')+1);
                JOptionPane.showMessageDialog(null, message,"Уведомление",JOptionPane.PLAIN_MESSAGE);
            } catch(FileNotFoundException exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(),"Ошибка",JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    class Analyse implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                analyser.clearTextFromStopSymbolsOfLanguages();
                analyser.analyse();
                analyser.sortFrequenciesByValue();
                String message = "Текст проанализирован";
                JOptionPane.showMessageDialog(null, message,"Уведомление",JOptionPane.PLAIN_MESSAGE);
            } catch(NullPointerException exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(),"Ошибка",JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    class Download implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                String pathText = chooseFile(true);
                String pathFrequencies = chooseFile(false);
                String message = "Данные экспортированы в "+pathText+" и "+pathFrequencies;
                JOptionPane.showMessageDialog(null, message,"Уведомление",JOptionPane.PLAIN_MESSAGE);
            } catch(FileNotFoundException exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(),"Ошибка",JOptionPane.PLAIN_MESSAGE);
            }
        }
        // --------------Write -> TextWriter & FrequenciesWriter
        private String chooseFile(boolean chooseText) throws FileNotFoundException{
            String defaultPath = "D:\\_Mehi\\6sem\\java\\analyser\\src\\main\\resources\\out\\";
            JFileChooser fileChooser = new JFileChooser(defaultPath);
            File file = new File(defaultPath+(chooseText ? "newText.txt" : "frequencies.xlsx"));
            fileChooser.setCurrentDirectory(file);
            String dialogMessage = chooseText ? "текст" : "частотный анализ";
            int ret = fileChooser.showDialog(null, "Сохранить "+dialogMessage);
            String path = "";
            if(ret == JFileChooser.APPROVE_OPTION){
                path = fileChooser.getSelectedFile().getAbsolutePath();
            }
            if(path.equals("")) throw new FileNotFoundException("Выберите файл");
            Writer writer = new Writer(path, analyser);
            if(chooseText) writer.saveText();
            else writer.saveFrequencies();
            return path.substring(path.lastIndexOf('\\')+1);
        }
    }

    class UploadStoplist implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                String path = "";
                JFileChooser fileChooser = new JFileChooser("D:\\_Mehi\\6sem\\java\\analyser\\src\\main\\resources");
                int ret = fileChooser.showDialog(null, "Выбрать файл");
                if(ret == JFileChooser.APPROVE_OPTION) path = fileChooser.getSelectedFile().getAbsolutePath();
                if(path.equals("")) throw new FileNotFoundException("Выберите файл");
                stopSymbolsReader = new Reader(path);
                stopSymbolsReader.read();
                Language language = new Language(stopSymbolsReader.getText().get(0));
                language.generateStopSymbols(stopSymbolsReader);
                analyser.addLanguage(language);
                String message = "Данные импортированы из "+path.substring(path.lastIndexOf('\\')+1);
                JOptionPane.showMessageDialog(null, message,"Уведомление",JOptionPane.PLAIN_MESSAGE);
            } catch(FileNotFoundException exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(),"Ошибка",JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    class LanguagesList implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                JFrame frame = new JFrame("Languages");
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                JPanel checkBoxesPanel = new JPanel();
                for(Map.Entry entry : analyser.getLanguages().entrySet()){
                    Language language = (Language) entry.getKey();
                    boolean active = (boolean) entry.getValue();
                    JCheckBox checkBox = new JCheckBox(language.getCode());
                    checkBox.setSelected(active);
                    checkBoxesPanel.add(checkBox);
                    checkBox.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            toggleLanguage(language);
                            checkBox.setSelected(!active);
                        }
                    });
                }
                panel.add(checkBoxesPanel, BorderLayout.SOUTH);
                setPreferredSize(new Dimension(250, 200));
                frame.setContentPane(panel);
                frame.setBounds(200,200,300,400);
                frame.setVisible(true);
            } catch(Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(),"Ошибка",JOptionPane.PLAIN_MESSAGE);
            }
        }
        private void toggleLanguage(Language language){
            // toggle value of language
            var languages = analyser.getLanguages();
            languages.put(language, !languages.get(language));
        }
    }

/*
    class ButtonPrintEventListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                if(reactorList == null) throw new Exception("Нет данных");
                DefaultMutableTreeNode reactorTree = new DefaultMutableTreeNode("Reactor", true);
                DefaultMutableTreeNode reactorBranch = null;
                for(Reactor reactor : reactorList.getReactors()){
                    reactorBranch = new DefaultMutableTreeNode(reactor.getType(), true);
                    reactorBranch.add(new DefaultMutableTreeNode("burnup = "+reactor.getBurnup(), false));
                    reactorBranch.add(new DefaultMutableTreeNode("kpd = "+reactor.getKpd(), false));
                    reactorBranch.add(new DefaultMutableTreeNode("enrichment = "+reactor.getEnrichment(), false));
                    reactorBranch.add(new DefaultMutableTreeNode("termalCapacity = "+reactor.getTermalCapacity(), false));
                    reactorBranch.add(new DefaultMutableTreeNode("electricalCapacity = "+reactor.getElectricalCapacity(), false));
                    reactorBranch.add(new DefaultMutableTreeNode("lifeTime = "+reactor.getLifeTime(), false));
                    reactorBranch.add(new DefaultMutableTreeNode("firstLoad = "+reactor.getFirstLoad(), false));
                    reactorTree.add(reactorBranch);
                }
                tree = new JTree(reactorTree);
                tree.setShowsRootHandles(true);
                //
                JFrame infoFrame = new JFrame("info");
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BorderLayout());
                infoFrame.setContentPane(infoPanel);
                infoFrame.setBounds(200,200,300,400);
                infoFrame.add(tree);
                infoFrame.add(new JScrollPane(tree));
                infoFrame.setVisible(true);
            } catch(Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(),"Вывод",JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
*/

    class Exit implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }

}
