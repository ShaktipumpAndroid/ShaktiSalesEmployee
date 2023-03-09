package bean;

import java.io.File;

/**
 * Created by shakti on 12/8/2016.
 */
public class CameraFileBean {
    static File file;

    public static File getFile() {
        return file;
    }

    public static void setFile(File fileName) {
        file = fileName;
    }
}
