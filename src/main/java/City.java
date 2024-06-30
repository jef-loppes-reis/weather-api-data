public class City {
    private final String name; // Nome da cidade
    private final double latitude; // Latitude da cidade
    private final double longitude; // Longitude da cidade

    // Construtor para inicializar os atributos da cidade
    public City(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Método para obter o nome da cidade
    public String getName() {
        return name;
    }

    // Método para obter a latitude formatada como string com ponto decimal
    public String getLatitude() {
        return String.format("%.4f", latitude).replace(',', '.');
    }

    // Método para obter a longitude formatada como string com ponto decimal
    public String getLongitude() {
        return String.format("%.4f", longitude).replace(',', '.');
    }
}
