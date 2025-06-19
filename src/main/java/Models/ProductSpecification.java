package Models;

public class ProductSpecification {
    private int specId;            
    private String productId;      
    private String cpu;            
    private String ram;            
    private String storage;        
    private String screen;         
    private String os;             
    private String battery;        
    private String camera;         
    private String graphic;       

   
    public ProductSpecification() {
    }

    
    public ProductSpecification(int specId, String productId, String cpu, String ram, String storage,
                                String screen, String os, String battery, String camera, String graphic) {
        this.specId = specId;
        this.productId = productId;
        this.cpu = cpu;
        this.ram = ram;
        this.storage = storage;
        this.screen = screen;
        this.os = os;
        this.battery = battery;
        this.camera = camera;
        this.graphic = graphic;
    }

    

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getGraphic() {
        return graphic;
    }

    public void setGraphic(String graphic) {
        this.graphic = graphic;
    }
}
