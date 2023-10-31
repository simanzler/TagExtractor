import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;

public class TagExtractor extends JFrame {
    JPanel mainPnl;
    JPanel topPnl;
    JPanel middlePnl;
    JPanel bottomPnl;
    JTextField field1;
    JTextField field2;
    JTextField field3;
    JButton file1;
    JButton file2;
    JButton create;
    JTextArea textArea;
    JScrollPane scrollPane;
    Set<String> stopWords = new HashSet<>();
    Map<String, Integer> map = new HashMap<>();
    public TagExtractor(){
        mainPnl = new JPanel(new BorderLayout());

        CreateTopPanel();
        mainPnl.add(topPnl, BorderLayout.NORTH);
        CreateMiddlePanel();
        mainPnl.add(middlePnl, BorderLayout.CENTER);
        CreateBottomPanel();
        mainPnl.add(bottomPnl, BorderLayout.SOUTH);

        add(mainPnl);

        setTitle("Tag Extractor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        pack();
    }
    public void CreateTopPanel(){
        topPnl = new JPanel();
        field1 = new JTextField("Choose text file to read",20);
        field1.setEditable(false);
        field2 = new JTextField("Choose stop word text file",20);
        field2.setEditable(false);
        file1 = new JButton("File");
        file2 = new JButton("File");

        textArea = new JTextArea(10,40);
        scrollPane = new JScrollPane(textArea);

        file1.addActionListener((ActionEvent ae) -> {
            JFileChooser chooser = new JFileChooser();
            File selectedFile;
            String rec = "";
            try
            {

                File workingDirectory = new File(System.getProperty("user.dir"));
                chooser.setCurrentDirectory(workingDirectory);

                if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    selectedFile = chooser.getSelectedFile();
                    Path file = selectedFile.toPath();
                    InputStream in =
                            new BufferedInputStream(Files.newInputStream(file, CREATE));
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(in));

                    int line = 0;
                    while(reader.ready())
                    {
                        rec = reader.readLine();
                        line++;
                        String[] words = rec.split("\\W+");
                        for (String word: words) {
                            String lword = word.toLowerCase();
                            if((!stopWords.contains(lword))&&(word.length()>1)){
                                if(!map.containsKey(lword)){
                                    map.put(lword, 1);
                                }else{
                                    map.put(lword, map.get(lword) + 1);
                                }
                            }
                        }

                    }
                    reader.close();
                    System.out.println("\n\nData file read!");
                    field1.setText(selectedFile.getName());
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        textArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
                    }
                }
            }
            catch (FileNotFoundException e)
            {
                System.out.println("File not found!!!");
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });
        file2.addActionListener((ActionEvent ae) -> {
            JFileChooser chooser = new JFileChooser();
            File selectedFile;
            String rec = "";
            try
            {

                File workingDirectory = new File(System.getProperty("user.dir"));
                chooser.setCurrentDirectory(workingDirectory);

                if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    selectedFile = chooser.getSelectedFile();
                    Path file = selectedFile.toPath();
                    InputStream in =
                            new BufferedInputStream(Files.newInputStream(file, CREATE));
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(in));

                    int line = 0;
                    while(reader.ready())
                    {
                        rec = reader.readLine();
                        line++;
                        String[] words = rec.split("\\W+");
                        stopWords.addAll(Arrays.asList(words));

                    }
                    reader.close();
                    System.out.println("\n\nData file read!");
                    field2.setText(selectedFile.getName());
                }
            }
            catch (FileNotFoundException e)
            {
                System.out.println("File not found!!!");
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        topPnl.add(field2);
        topPnl.add(file2);
        topPnl.add(field1);
        topPnl.add(file1);
    }
    public void CreateMiddlePanel(){
        middlePnl = new JPanel();

        middlePnl.add(scrollPane);
    }
    public void CreateBottomPanel(){
        bottomPnl = new JPanel();
        field3 = new JTextField("Enter name of file to create",20);
        create = new JButton("Create");

        create.addActionListener((ActionEvent ae) -> {
            File workingDirectory = new File(System.getProperty("user.dir"));
            Path file = Paths.get(workingDirectory.getPath() + "\\src\\" + field3.getText() + ".txt");

            try
            {
                OutputStream out =
                        new BufferedOutputStream(Files.newOutputStream(file, CREATE));
                BufferedWriter writer =
                        new BufferedWriter(new OutputStreamWriter(out));
                String word;
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    word = (entry.getKey() + ": " + entry.getValue());
                    writer.write(word, 0, word.length());
                    writer.newLine();
                }
                writer.close();
                System.out.println("Data file written!");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

        bottomPnl.add(field3);
        bottomPnl.add(create);
    }
    public static void main(String[] args){
        JFrame frame = new TagExtractor();
    }
}
