package Processors;

import Entities.Controllers;
import ViewControllers.MainController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Created by arxa on 16/11/2016.
 */

public class FileProcessor
{
    public static void chooseVideoFile()
    {
        File chosenFile = FileProcessor.showFileDialog();
        if (chosenFile == null) return;
        MainController.setCurrentVideoFile(chosenFile);
        if (!FileProcessor.validateVideoFileName(MainController.getCurrentVideoFile())){
            new Alert(Alert.AlertType.WARNING, "ERROR on loading file\n"+
                    "Couldn't load file specified", ButtonType.OK).showAndWait();
            return;
        }
        if (!Player.playVideo(MainController.getCurrentVideoFile())){
            new Alert(Alert.AlertType.WARNING, "ERROR on loading file\n"+
                    "Please choose a valid .mp4 video file", ButtonType.OK).showAndWait();
            return;
        }
        if (!FileProcessor.createDirectories()){
            new Alert(Alert.AlertType.WARNING, "ERROR on loading file\n"+
                    "Failed to create directories", ButtonType.OK).showAndWait();
            return;
        }
        MainController.resizeStageSlowly(1150, true);
    }

    public static boolean createDirectories()
    {
        try {
            // Generating unique name of current video file operation
            ImageWriter.setUniqueFolderName(MainController.getCurrentVideoFile().getName().replace(".mp4","")+" "+
                    new Date().toString().replace(":","-"));

            // Creating paths for write operations
            Files.createDirectories(Paths.get(ImageWriter.getFolderPath()
                    + ImageWriter.getUniqueFolderName() + "\\Text Blocks"));
            Files.createDirectories(Paths.get(ImageWriter.getFolderPath()
                    + ImageWriter.getUniqueFolderName() + "\\Painted Frames"));
            Files.createDirectories(Paths.get(ImageWriter.getFolderPath()
                    + ImageWriter.getUniqueFolderName() + "\\Steps"));
            Files.createDirectories(Paths.get(ImageWriter.getFolderPath()
                    + ImageWriter.getUniqueFolderName() + "\\Video"));
            Files.createDirectories(Paths.get(ImageWriter.getFolderPath()
                    + ImageWriter.getUniqueFolderName() + "\\OCR Images"));
            return true;
        }
        catch (RuntimeException | IOException ex) {
            return false;
        }
    }

    public static boolean validateVideoFileName(File filename) {
        return filename != null && filename.exists();
    }

    public static File showFileDialog()
    {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose video file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Video Files", "*.mp4"));
        return fileChooser.showOpenDialog(stage);
    }

    public static void loadLibraries() throws IOException
    {


        try {
            String currentDirectory = new File(".").getCanonicalPath();
            System.load(currentDirectory + "\\src\\main\\resources\\DLLs\\openh264-1.6.0-win64msvc.dll");
            Controllers.getLogController().logTextArea.appendText("Loaded openh254 FFMPEG library\n");

            String osName = System.getProperty("os.name");
            if(osName.startsWith("Windows"))
            {
                int bit = Integer.parseInt(System.getProperty("sun.arch.data.model"));
                if(bit == 32){
                    System.load(currentDirectory + "\\src\\main\\resources\\DLLs\\opencv_java320.dll");
                    Controllers.getLogController().logTextArea.appendText("Loaded OpenCV for "+osName+" "+bit+"-bit\n");
                }
                else if (bit == 64){
                    System.load(currentDirectory + "\\src\\main\\resources\\DLLs\\opencv_java320.dll");
                    Controllers.getLogController().logTextArea.appendText("Loaded OpenCV for "+osName+" "+bit+"-bit\n");
                }
                else{
                    System.load(currentDirectory + "\\src\\main\\resources\\DLLs\\opencv_java320.dll");
                    Controllers.getLogController().logTextArea.appendText("Loaded OpenCV for "+osName+" "+bit+"-bit\n");
                }
            }
            else if(osName.equals("Mac OS X")){
                System.load(currentDirectory + "\\src\\main\\resources\\DLLs\\opencv_java320.dll");
                Controllers.getLogController().logTextArea.appendText("Loaded OpenCV for "+osName+"\n");
            }
        } catch (Throwable e) {
            Controllers.getLogController().logTextArea.appendText("Failed to load opencv native library: " + e.getMessage()+"\n");
            MainController.getLogStage().show();
        }
    }
}