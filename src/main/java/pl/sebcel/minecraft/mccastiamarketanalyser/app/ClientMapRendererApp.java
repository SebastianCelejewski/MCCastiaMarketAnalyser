package pl.sebcel.minecraft.mccastiamarketanalyser.app;

import java.io.File;

public class ClientMapRendererApp {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("usage: java -jar cma.java <data_directory_path>");
            System.exit(-1);
        }

        String inputDirectoryPath = args[0];

        new ClientMapRendererApp().run(inputDirectoryPath);
    }

    public void run(String inputDirectoryPath) {
        File inputDirectory = new File(inputDirectoryPath);
    }

}