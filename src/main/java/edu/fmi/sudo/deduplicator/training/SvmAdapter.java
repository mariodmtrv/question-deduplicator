package edu.fmi.sudo.deduplicator.training;

import java.io.File;
import java.io.IOException;

public abstract class SvmAdapter {
    protected String executablePath;
    protected String[] params;
    protected Long identifier;
    protected String resourcesRootPath = "src\\main\\resources\\feature-vectors\\";
    protected String modelPath = resourcesRootPath + "models\\generated-%s.model";

    public SvmAdapter(Long identifier) {
        this.identifier = identifier;
    }

    private void execute(String[] params) {
        try {
            Runtime.getRuntime().exec(this.executablePath, params, new File(new File(this.executablePath).getParent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(){
        execute(this.params);
    }
}
