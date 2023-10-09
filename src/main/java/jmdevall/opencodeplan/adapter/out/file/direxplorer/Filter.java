package jmdevall.opencodeplan.adapter.out.file.direxplorer;

import java.io.File;

public interface Filter {
    boolean interested(int level, String path, File file);
}