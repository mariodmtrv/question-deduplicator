package edu.fmi.sudo.deduplicator.training;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class SvmAdapter {
    protected String executablePath;
    protected String[] params;
    protected Long identifier;
    protected String resourcesRootPath = new File(System.getProperty("user.dir")).getParent() + File.separator + "feature-vectors\\";
    protected String modelPath = resourcesRootPath + "models\\generated-%s.model";

    public SvmAdapter(Long identifier) {
        this.identifier = identifier;
    }

    private void execute(String[] params) {
        try {
            String command = String.format("%s %s %s %s",
                    this.executablePath,
                    params[0],
                    params[1],
                    params[2]);

            Process process = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        execute(this.params);
    }
}
