class CustomerDto{
    private String id;
    private String name;
    private String address;

    CustomerDto(String id,String name,String address){
        this.id = id;
        this.name = name;
        this.address = address;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {}

    public String getName() {
        return name;
    }
    public void setName(String name) {}
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {}
    @Override
    public String toString() {
        return "CustomerDto{" + "id=" + id + ", name=" + name + ", address=" + address + '}';
    }
}