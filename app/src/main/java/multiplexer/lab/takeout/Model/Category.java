package multiplexer.lab.takeout.Model;

public class Category {

    String name,image,id;
    int catid;

    public Category(String name, String image, String id, int catid) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.catid = catid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }
}
