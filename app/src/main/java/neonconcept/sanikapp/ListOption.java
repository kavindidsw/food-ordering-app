package neonconcept.sanikapp;

public class ListOption {
    private int hotel_id;
    private String hotel_name;

    public ListOption(int param_id, String param_hotel_name){
        this.hotel_id = param_id;
        this.hotel_name = param_hotel_name;
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
}
