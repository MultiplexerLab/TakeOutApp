package multiplexer.lab.takeout.Model;

public class Ad {
    String pic;
    String message;

    public Ad(String pic, String message) {
        this.pic = pic;
        this.message = message;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
