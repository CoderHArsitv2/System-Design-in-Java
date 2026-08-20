import java.util.ArrayList;
import java.util.List;

//abstract class for FileSystemNode
abstract class FileSystemNode {
public abstract void display();
}

class File extends FileSystemNode{
    private String name;
    private int size;

    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public void display(){
        System.out.println("File: " + name + " Size: " + size);
    }
}

class Directory extends FileSystemNode{
    private String name;
    private List<FileSystemNode> children;

    public Directory(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void add(FileSystemNode node) {
        children.add(node);
    }

    public void remove(FileSystemNode node) {
        children.remove(node);
    }

    @Override
    public void display(){
        System.out.println("Directory: " + name);
        for (FileSystemNode node : children) {
            node.display();
        }
    }
}

public class fileSystemCompositeDesignPattern {

    public static void main(String[] args){
        //create a file system
        Directory root = new Directory("root");
        Directory dir1 = new Directory("dir1");
        Directory dir2 = new Directory("dir2");
        File file1 = new File("file1", 10);
        File file2 = new File("file2", 20);
        File file3 = new File("file3", 30);

        root.add(dir1);
        root.add(dir2);
        dir1.add(file1);
        dir1.add(file2);
        dir2.add(file3);

        root.display();
    }
}