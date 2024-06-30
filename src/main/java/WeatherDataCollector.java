import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WeatherDataCollector {

    private static final String API_URL_TEMPLATE = "https://historical-forecast-api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&start_date=%s&end_date=%s&hourly=temperature_2m";
    private static final int NUM_THREADS = 4; // Número de threads para execução paralela

    public static void main(String[] args) {
        // Captura o tempo de início
        long startTime = System.currentTimeMillis();

        // Lista de cidades com suas respectivas coordenadas
        List<City> cities = List.of(
                new City("Aracaju", -10.9167, -37.05),
                new City("Belém", -1.4558, -48.5039),
                new City("Belo Horizonte", -19.9167, -43.9333),
                new City("Boa Vista", 2.81972, -60.67333),
                new City("Brasília", -15.7939, -47.8828),
                new City("Campo Grande", -20.44278, -54.64639),
                new City("Cuiabá", -15.5989, -56.0949),
                new City("Curitiba", -25.4297, -49.2711),
                new City("Florianópolis", -27.5935, -48.55854),
                new City("Fortaleza", -3.7275, -38.5275),
                new City("Goiânia", -16.6667, -49.25),
                new City("João Pessoa", -7.12, -34.88),
                new City("Macapá", 0.033, -51.05),
                new City("Maceió", -9.66583, -35.73528),
                new City("Manaus", -3.1189, -60.0217),
                new City("Natal", -5.7833, -35.2),
                new City("Palmas", -10.16745, -48.32766),
                new City("Porto Alegre", -30.0331, -51.23),
                new City("Porto Velho", -8.76194, -63.90389),
                new City("Recife", -8.05, -34.9),
                new City("Rio Branco", -9.97472, -67.81),
                new City("Rio de Janeiro", -22.9111, -43.2056),
                new City("Salvador", -12.9747, -38.4767),
                new City("São Luís", -2.5283, -44.3044),
                new City("São Paulo", -23.55, -46.6333),
                new City("Teresina", -5.08917, -42.80194),
                new City("Vitória", -20.2889, -40.3083)
        );

        // Executor para gerenciar threads
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<CityWeatherData>> futures = new ArrayList<>();

        // Submete uma tarefa para cada cidade para buscar os dados meteorológicos
        for (City city : cities) {
            Callable<CityWeatherData> task = () -> fetchWeatherData(city);
            futures.add(executor.submit(task));
        }

        // Encerra o executor
        executor.shutdown();

        // Processa os resultados de cada tarefa
        for (Future<CityWeatherData> future : futures) {
            try {
                CityWeatherData data = future.get();
                if (data != null) {
                    System.out.println(data);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Captura o tempo de fim
        long endTime = System.currentTimeMillis();

        // Calcula o tempo total de execução
        long totalTime = endTime - startTime;

        // Imprime o tempo total de execução
        System.out.println("Tempo total de execução: " + totalTime + " ms");
    }

    // Método para buscar dados meteorológicos de uma cidade
    private static CityWeatherData fetchWeatherData(City city) throws Exception {
        String startDate = "2024-01-01"; // Data de início
        String endDate = "2024-01-31";   // Data de fim
        // Formata a URL da API com as coordenadas da cidade e as datas
        String url = String.format(API_URL_TEMPLATE, city.getLatitude(), city.getLongitude(), startDate, endDate);

        // Log da URL usada na requisição
        System.out.println("Fetching data for: " + city.getName());
        System.out.println("URL: " + url);

        // Cria e envia a requisição HTTP
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        // Log da resposta da API
        System.out.println("Response de " + city.getName() + ": " + responseBody);

        // Processa os dados meteorológicos da resposta
        return processWeatherData(responseBody, city);
    }

    // Método para processar os dados meteorológicos
    private static CityWeatherData processWeatherData(String responseBody, City city) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(responseBody); // Parseia a resposta JSON
        JSONObject hourly = (JSONObject) jsonObject.get("hourly"); // Obtém o objeto "hourly"

        // Verifica se o objeto "hourly" existe
        if (hourly == null) {
            System.err.println("No hourly data found for " + city.getName());
            return null;
        }

        // Obtém o array de temperaturas horárias
        JSONArray temperatures = (JSONArray) hourly.get("temperature_2m");

        // Verifica se o array de temperaturas existe
        if (temperatures == null) {
            System.err.println("No temperature data found for " + city.getName());
            return null;
        }

        double[][] dailyTemperatures = new double[31][24]; // Array para armazenar temperaturas diárias (31 dias, 24 horas por dia)
        for (int i = 0; i < temperatures.size(); i++) {
            // Verificação adicionada para valores nulos
            if (temperatures.get(i) != null) {
                int day = i / 24;  // Calcula o dia baseado no índice
                int hour = i % 24; // Calcula a hora baseado no índice
                dailyTemperatures[day][hour] = ((Number) temperatures.get(i)).doubleValue(); // Armazena a temperatura
            }
        }

        double[] dailyAvgTemp = new double[31]; // Array para médias diárias
        double[] dailyMinTemp = new double[31]; // Array para mínimas diárias
        double[] dailyMaxTemp = new double[31]; // Array para máximas diárias

        // Calcula as temperaturas médias, mínimas e máximas diárias
        for (int day = 0; day < 31; day++) {
            double sum = 0;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;

            for (int hour = 0; hour < 24; hour++) {
                double temp = dailyTemperatures[day][hour];
                sum += temp;
                if (temp < min) min = temp;
                if (temp > max) max = temp;
            }

            dailyAvgTemp[day] = sum / 24; // Calcula a média diária
            dailyMinTemp[day] = min;      // Obtém a mínima diária
            dailyMaxTemp[day] = max;      // Obtém a máxima diária
        }

        // Retorna os dados meteorológicos da cidade
        return new CityWeatherData(city.getName(), dailyAvgTemp, dailyMinTemp, dailyMaxTemp);
    }
}
