public class CityWeatherData {
    private final String cityName; // Nome da cidade
    private final double[] dailyAvgTemp; // Array de temperaturas médias diárias
    private final double[] dailyMinTemp; // Array de temperaturas mínimas diárias
    private final double[] dailyMaxTemp; // Array de temperaturas máximas diárias

    // Construtor para inicializar os atributos com os dados meteorológicos
    public CityWeatherData(String cityName, double[] dailyAvgTemp, double[] dailyMinTemp, double[] dailyMaxTemp) {
        this.cityName = cityName;
        this.dailyAvgTemp = dailyAvgTemp;
        this.dailyMinTemp = dailyMinTemp;
        this.dailyMaxTemp = dailyMaxTemp;
    }

    // Método para formatar os dados meteorológicos em uma string legível
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n" + "Dados meteorológicos de ").append(cityName).append(":\n");
        sb.append("Dia\tT.Media\tT.Min\tT.Max\n");
        for (int i = 0; i < dailyAvgTemp.length; i++) {
            sb.append(i + 1).append("\t")
                    .append(String.format("%.2f", dailyAvgTemp[i])).append("\t")
                    .append(String.format("%.2f", dailyMinTemp[i])).append("\t")
                    .append(String.format("%.2f", dailyMaxTemp[i])).append("\n");
        }
        return sb.toString();
    }
}
