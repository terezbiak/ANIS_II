package sk.tuke.zadanie_zajko;

public class LocationCoordinates {
    private double latitude;
    private double longitude;

    public LocationCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

