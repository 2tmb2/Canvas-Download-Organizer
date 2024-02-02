import javax.swing.JFileChooser;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
//import java.awt.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.Files;

public class CanvasFileDownloadOrganizer extends JPanel
{
    final JFileChooser fc = new JFileChooser();
    public String chooseFolder()
    {
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            return fc.getSelectedFile().toString();
        }
        return null;
    }
    public File chooseFile()
    {
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new FileNameExtensionFilter("text or spreadsheets", "txt", "csv"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            return fc.getSelectedFile();
        }
        return null;
    }
    
    public static void main(String[] args) throws IOException
    {
        JFrame frame = new JFrame("tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CanvasFileDownloadOrganizer panel = new CanvasFileDownloadOrganizer();
        frame.getContentPane().add(panel,"Center");
        JOptionPane.showMessageDialog(frame, "Select the folder that contains the files");
        String moveFrom = panel.chooseFolder();
        JOptionPane.showMessageDialog(frame, "Select the file that contains Student's names in the format lastfirst");
        File names = panel.chooseFile();
        JOptionPane.showMessageDialog(frame, "Select the folder where you'd like to store all the organized-by-name files");
        String storeLocation = panel.chooseFolder();
        String fileExtension = JOptionPane.showInputDialog(frame, "What file type? E.G. .java, .a3p, etc. Make sure you include the dot.");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(panel.getPreferredSize());
        frame.setVisible(true);
        File listOfFiles = new File(moveFrom);
        String contents[] = listOfFiles.list();
        System.out.println(names.getAbsolutePath().substring(names.getAbsolutePath().length() -4 ));
        if (names.getAbsolutePath().substring(names.getAbsolutePath().length() - 4) == ".txt")
        {
            List<String> list = Files.readAllLines(Paths.get(names.getAbsolutePath()));
            String[] a = list.toArray(new String[list.size()]);
            int filesMoved = 0;
            try
            {
                for (int j = 0; j < a.length; j++)
                {
                    for (int i = 0; i < contents.length; i++)
                    {
                        if (contents[i].contains(a[j]))
                        {
                            // the file at contents[i] goes with the name at a[j]
                            try
                            {
                                new File(storeLocation + "/" + a[j].toString()).mkdirs();
                                String name = contents[i].toString().substring(contents[i].toString().indexOf("_", contents[i].toString().indexOf("_", contents[i].toString().indexOf("_") + 1) + 1) + 1, contents[i].toString().indexOf("."));
                                Path temp = Files.move(Paths.get(moveFrom + "/" + contents[i].toString()), Paths.get(storeLocation + "/" + a[j].toString() + "/" + name + fileExtension),StandardCopyOption.REPLACE_EXISTING);
                            }
                            catch(Exception e) {
                                System.out.println("File transfer failed with exception " + e);
                            }
                            filesMoved += 1;
                        }
                    }
                    
                }
                JOptionPane.showMessageDialog(frame, "Success! " + filesMoved + " files were transferred and renamed!");
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(frame, "file transfer failed with the exception " + e);
            }
        }
        else
        {
            List<String> list = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(names.getAbsolutePath()))) {
                List<String> firstColumnData = new ArrayList<>();
                for (int i = 0; i < 2; i++)
                {
                    br.readLine();
                }
                String line;
                while ((line = br.readLine()) != null)
                {
                    System.out.println(line);
                    String[] values = line.split(",\\s*");
                    if (values.length > 0)
                    {
                        System.out.println(values.toString());
                        String value = (values[0].trim().replaceAll("\"", "").replaceAll("'","").toLowerCase() + values[1].trim().replaceAll("'","").replaceAll("\"", "")).toLowerCase();
                        System.out.println(value);
                        firstColumnData.add(value);
                    }
                }
                for (String value : firstColumnData)
                {
                    list.add(value);
                }
                String[] a = list.toArray(new String[list.size()]);
                int filesMoved = 0;
                Arrays.toString(a);
                try
                {
                    for (int j = 0; j < a.length; j++)
                    {
                        for (int i = 0; i < contents.length; i++)
                        {
                            if (contents[i].contains(a[j]))
                            {
                                // the file at contents[i] goes with the name at a[j]
                                try
                                {
                                    new File(storeLocation + "/" + a[j].toString()).mkdirs();
                                    String name = contents[i].toString().substring(contents[i].toString().indexOf("_", contents[i].toString().indexOf("_", contents[i].toString().indexOf("_") + 1) + 1) + 1, contents[i].toString().indexOf("."));
                                    Path temp = Files.move(Paths.get(moveFrom + "/" + contents[i].toString()), Paths.get(storeLocation + "/" + a[j].toString() + "/" + name + fileExtension),StandardCopyOption.REPLACE_EXISTING);
                                }
                                catch(Exception e) {
                                    System.out.println("File transfer failed with exception " + e);
                                }
                                filesMoved += 1;
                            }
                        }
                        
                    }
                    JOptionPane.showMessageDialog(frame, "Success! " + filesMoved + " files were transferred and renamed!");
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(frame, "file transfer failed with the exception " + e);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        frame.dispose();
    }
}
