package shafi.sbf.com.foodjinni;

class CatagoriPojo {

    private String id;
    private String catagori;

    public CatagoriPojo() {

    }

    public CatagoriPojo(String id, String catagori) {
        this.id = id;
        this.catagori = catagori;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatagori() {
        return catagori;
    }

    public void setCatagori(String catagori) {
        this.catagori = catagori;
    }
}
