import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class myFrame extends JFrame implements ActionListener {

    JButton fileChooserButton;
    JTextArea textArea;
    myFrame(){
        this.setTitle("Common Word Finder");
        this.setSize(800,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        //scrollPane start
        textArea = new JTextArea();
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(400, 50, 375, 700);
        //scrollPane end

        //scrollPane Label start
        JLabel wordLabel = new JLabel();
        wordLabel.setBounds(400, 22, 300, 100);
        wordLabel.setText("Most Common Words");
        wordLabel.setHorizontalAlignment(JLabel.LEFT);
        wordLabel.setVerticalAlignment(JLabel.TOP);
        wordLabel.setFont(new Font(wordLabel.getFont().getName(), Font.PLAIN, 18));
        //scrollPane Label end

        //introLabel start
        JLabel introLabel = new JLabel();
        introLabel.setBounds(0, 0, 800, 100);
        introLabel.setText("<html><div style='padding-left: 25px;'><br>Welcome to the Common Word Finder!" +
                "<br>Choose file or drag it into the dropbox.</div></html>");
        introLabel.setHorizontalAlignment(JLabel.LEFT);
        introLabel.setVerticalAlignment(JLabel.TOP);
        introLabel.setFont(new Font(introLabel.getFont().getName(), Font.PLAIN, 18));
        //introLabel end

        //fileChooser start
        fileChooserButton = new JButton("Choose Text File");
        fileChooserButton.setBounds(25, 80, 350, 50);
        fileChooserButton.addActionListener(this);
        //fileChooser end
        this.add(introLabel);
        this.add(wordLabel);
        this.add(fileChooserButton);
        this.add(scrollPane);
        this.getContentPane().setBackground(new Color(161,158,157));
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==fileChooserButton){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("~"));
            //returns 0 if file successfully chosen
            int response = fileChooser.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                Path path = Path.of(fileChooser.getSelectedFile().getAbsolutePath());
                if(!isTxtFile(path)){
                    //todo
                }
                else{
                    System.out.println("Text File Entered.");
                    int limit = 20;
                    String fileName = path.getFileName().toString();
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    MyMap<String, Integer> map = new MyHashMap<>();
                    try{
                        CommonWordFinder.parseAndStoreWords(file, map);
                    }
                    // catches an IO exception when reading
                    catch(IOException er){
                        //todo
                        System.err.println("Error: An I/O error occurred reading " + fileName + ".");
                    }
                    Entry<String,Integer>[] arrayOfWords = CommonWordFinder.mostCommonWords(map);
                    if(map.size() < limit){
                        limit = map.size();
                    }
                    String displayText = CommonWordFinder.stringOfMostCommonWords(limit, arrayOfWords);
                    textArea.setText(displayText);
                    this.revalidate();
                    this.repaint();
                }
            }
        }
    }

    public boolean isTxtFile(Path path){
        String fileName = path.getFileName().toString();
        return fileName.endsWith(".txt");
    }
}
