package multiplexer.lab.takeout.Model;

public class Product {

    int id, rating, customer_rating, price, countryid;
    String name, image, description;

    public Product(int id, int rating, int customer_rating, int price, int countryid, String name, String image, String description) {
        this.id = id;
        this.rating = rating;
        this.customer_rating = customer_rating;
        this.price = price;
        this.countryid = countryid;
        this.name = name;
        this.image = image;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getCustomer_rating() {
        return customer_rating;
    }

    public void setCustomer_rating(int customer_rating) {
        this.customer_rating = customer_rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCountryid() {
        return countryid;
    }

    public void setCountryid(int countryid) {
        this.countryid = countryid;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
