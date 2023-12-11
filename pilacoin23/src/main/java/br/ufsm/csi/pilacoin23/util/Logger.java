package br.ufsm.csi.pilacoin23.util;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    public void gravarLogEmArquivo(String logMessage) {
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.write(logMessage + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
