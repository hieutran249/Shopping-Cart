package hanu.a2_2001040078.models;

public class CartItem {
    private Long id;
    private Long productId;
    private String name;
    private String thumbnail;
    private String category;
    private int unitPrice;
    private int quantity;

    public CartItem(Long id, Long productId, String name, String thumbnail, String category, int unitPrice, int quantity) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.thumbnail = thumbnail;
        this.category = category;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public CartItem(Long productId, String name, String thumbnail, String category, int unitPrice, int quantity) {
        this.productId = productId;
        this.name = name;
        this.thumbnail = thumbnail;
        this.category = category;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", category='" + category + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                '}';
    }
}
