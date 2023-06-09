import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class myFrame extends JFrame implements ActionListener {

    JButton fileChooserButton;
    JButton increaseButton;
    JButton decreaseButton;
    JTextArea textArea;
    JLabel countLabel;
    JLabel limitLabel;
    int limit = 40;

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

        //countLabel start
        countLabel = new JLabel();
        countLabel.setBounds(400, 22, 300, 100);
        countLabel.setText("Total Unique Words: ");
        countLabel.setHorizontalAlignment(JLabel.LEFT);
        countLabel.setVerticalAlignment(JLabel.TOP);
        countLabel.setFont(new Font(countLabel.getFont().getName(), Font.PLAIN, 18));
        //countLabel end

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

        //limitLabel start
        limitLabel = new JLabel();
        limitLabel.setBounds(30, 630, 250, 100);
        limitLabel.setText("Limit: " + limit);
        limitLabel.setFont(new Font(limitLabel.getFont().getName(), Font.PLAIN, 20));
        //limitLabel end

        //increaseButton start
        increaseButton = new JButton("<html><div style='text-align: center;'>"
                + "<font size='12'><b>&and;</b></font></div></html>");
        increaseButton.setBounds(150, 650, 65, 60);
        increaseButton.addActionListener(this);
        //increaseButton end

        //decreaseButton start
        decreaseButton = new JButton("<html><div style='text-align: center;'>"
                + "<font size='12'><b>&or;</b></font></div></html>");
        decreaseButton.setBounds(225, 650, 65, 60);
        decreaseButton.addActionListener(this);
        //decreaseButton end

        //DropBox start
        Border border = BorderFactory.createLineBorder(Color.GRAY, 2);
        JLabel dropLabel = new JLabel("Drag and drop file here.");
        dropLabel.setBounds(25, 150, 350, 450);
        dropLabel.setHorizontalAlignment(JLabel.CENTER);
        dropLabel.setVerticalAlignment(JLabel.CENTER);
        dropLabel.setBorder(border);
        dropLabel.setDropTarget(new DropTarget(){
            public synchronized void drop(DropTargetDropEvent evt) {
                evt.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transferable = evt.getTransferable();
                if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        if (files.size() == 1) {
                            // Do something with the dropped file(s)
                            String filePath = files.get(0).getAbsolutePath();
                            Path path = Path.of(filePath);
                            if(!isTxtFile(path)){
                                invalidFileMessage();
                            }
                            else{
                                handleDisplay(filePath);
                            }
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        //DropBox end

        this.add(dropLabel);
        this.add(introLabel);
        this.add(limitLabel);
        this.add(increaseButton);
        this.add(decreaseButton);
        this.add(countLabel);
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
                    textArea.setText("File provided is not a valid text file.");
                }
                else{
                    handleDisplay(path, fileChooser);
                }
                this.revalidate();
                this.repaint();
            }
        }

        if(e.getSource()==increaseButton){
            limit++;
            limitLabel.setText("Limit: " + limit);
            this.revalidate();
            this.repaint();
        }

        if(e.getSource()==decreaseButton){
            limit--;
            limitLabel.setText("Limit: " + limit);
            this.revalidate();
            this.repaint();
        }

    }

    public void handleDisplay(Path path, JFileChooser fileChooser){
        String fileName = path.getFileName().toString();
        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
        MyMap<String, Integer> map = new MyHashMap<>();
        try{
            CommonWordFinder.parseAndStoreWords(file, map);
        }
        // catches an IO exception when reading
        catch(IOException er){
            textArea.setText("Error: An I/O error occurred reading " + fileName + ".");
            this.revalidate();
            this.repaint();
        }
        Entry<String,Integer>[] arrayOfWords = CommonWordFinder.mostCommonWords(map);
        if(map.size() < limit){
            limit = map.size();
        }
        String displayText = CommonWordFinder.stringOfMostCommonWords(limit, arrayOfWords);
        textArea.setText(displayText);
        countLabel.setText("Total Unique Words: " + map.size());
    }

    public void handleDisplay(String filePath){
        Path path = Path.of(filePath);
        String fileName = path.getFileName().toString();
        File file = new File(filePath);
        MyMap<String, Integer> map = new MyHashMap<>();
        try{
            CommonWordFinder.parseAndStoreWords(file, map);
        }
        // catches an IO exception when reading
        catch(IOException er){
            textArea.setText("Error: An I/O error occurred reading " + fileName + ".");
            this.revalidate();
            this.repaint();
        }
        Entry<String,Integer>[] arrayOfWords = CommonWordFinder.mostCommonWords(map);
        if(map.size() < limit){
            limit = map.size();
        }
        String displayText = CommonWordFinder.stringOfMostCommonWords(limit, arrayOfWords);
        textArea.setText(displayText);
        countLabel.setText("Total Unique Words: " + map.size());
        this.revalidate();
        this.repaint();
    }
    public boolean isTxtFile(Path path){
        String fileName = path.getFileName().toString();
        return fileName.endsWith(".txt");
    }

    public void invalidFileMessage(){
        textArea.setText("File provided is not a valid text file.");
        this.revalidate();
        this.repaint();
    }


}
