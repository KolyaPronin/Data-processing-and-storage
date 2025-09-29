package Server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Finder {
    public boolean find(String name) {
        Path path = Paths.get(name + ".crt");
        return Files.exists(path);
    }
}
