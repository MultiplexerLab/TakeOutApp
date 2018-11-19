package multiplexer.lab.takeout.Model;

public class Menu {

    int shortPicId, longPicId;

    public Menu(int shortPicId, int longPicId) {
        this.shortPicId = shortPicId;
        this.longPicId = longPicId;
    }

    public int getShortPicId() {
        return shortPicId;
    }

    public void setShortPicId(int shortPicId) {
        this.shortPicId = shortPicId;
    }

    public int getLongPicId() {
        return longPicId;
    }

    public void setLongPicId(int longPicId) {
        this.longPicId = longPicId;
    }
}
