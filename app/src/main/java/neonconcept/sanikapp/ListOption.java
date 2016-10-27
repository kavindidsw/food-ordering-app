package neonconcept.sanikapp;

public class ListOption {
    private int hotel_id;
    private int vendor_id;
    private String hotel_name;
    private String reg_no;
    private String address;
    private String email;
    private String phone;
    private String phone_land;
    private String description;
    private String open_hrs;
    private String postal_code;

    public ListOption(int param_id, int p_vendor, String param_hotel_name, String pram_reg_no, String param_addr, String p_email, String p_phone, String p_land, String p_description,String p_openhrs, String p_postalcode){
        this.hotel_id = param_id;
        this.vendor_id = p_vendor;
        this.hotel_name = param_hotel_name;

        this.reg_no = pram_reg_no;
        this.address = param_addr;
        this.email = p_email;
        this.phone = p_phone;
        this.phone_land = p_land;
        this.description = p_description;
        this.open_hrs = p_openhrs;
        this.postal_code = p_postalcode;
    }

    @Override
    public String toString(){
        return getHotel_name();
    }


    public int getHotel_id() {
        return hotel_id;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public String getReg_no() {
        return reg_no;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoneLand() {
        return phone_land;
    }

    public String getDescription() {
        return description;
    }

    public String getOpen_hrs() {
        return open_hrs;
    }

    public String getPostal_code() {
        return postal_code;
    }
}
