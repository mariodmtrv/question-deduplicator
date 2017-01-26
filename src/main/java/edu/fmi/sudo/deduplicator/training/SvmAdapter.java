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
            String parentDir = new File(this.executablePath).getParent();
            Process process = Runtime.getRuntime().exec( this.executablePath, params, new File(parentDir));
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line = null;
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
