# Weather Data Collector

Este projeto coleta dados meteorológicos históricos para várias cidades brasileiras usando a API Open-Meteo. Ele busca temperaturas horárias para o mês de janeiro de 2024 e calcula temperaturas médias, mínimas e máximas diárias.

E possivel rodar o projeto com varias Threads, na linha de codigo mude a variavel `NUM_THREADS`, no final ele apresenta o temoo de executacao do projeto.

## Estrutura do Projeto

O projeto consiste em três arquivos principais:

1. `WeatherDataCollector.java`: Contém a lógica principal para buscar e processar os dados meteorológicos.
2. `City.java`: Representa uma cidade com nome, latitude e longitude.
3. `CityWeatherData.java`: Armazena os dados meteorológicos processados para uma cidade.

## Funcionamento
O programa realiza as seguintes etapas:

1. Define uma lista de cidades brasileiras com suas coordenadas. 
2. Usa um executor para buscar dados meteorológicos em paralelo para todas as cidades. 
3. Processa os dados retornados para calcular temperaturas médias, mínimas e máximas diárias. 
4. Imprime os resultados para cada cidade. 
5. Calcula e imprime o tempo total de execução do programa.


## Exemplo de Saída

```bash
Buscando dados para: Brasília
URL: https://api.open-meteo.com/v1/forecast?latitude=-15.7939&longitude=-47.8828&start_date=2024-01-01&end_date=2024-01-31&hourly=temperature_2m
Response para Brasília: {...}
Dados meteorológicos de Brasília:
Day  Avg Temp  Min Temp  Max Temp
1    25.50     20.00     30.00
2    26.00     21.00     31.00
...

Tempo total de execução: 1234 ms
```

## Requisitos

- Java 17 ou superior
- Biblioteca JSON Simple (disponível em `src/lib/json-simple-1.1.1.jar`)

## Observação
- Baixe a biblioteca `json-simple-1.1.1.jar` e coloque na pasta `src/lib` -> [download](https://code.google.com/archive/p/json-simple/downloads)

## Instruções de Uso

1. Clone o repositório para sua máquina local.
2. Certifique-se de que a biblioteca JSON Simple está disponível no caminho especificado.
3. Compile e execute o projeto usando seu IDE favorito ou via linha de comando.

### Compilação e Execução via Linha de Comando

```bash
# Navegue até o diretório do projeto
cd caminho/para/seu/projeto

# Compile os arquivos Java
javac -cp src/lib/json-simple-1.1.1.jar -d out src/main/java/*.java

# Execute o programa
java -cp out:src/lib/json-simple-1.1.1.jar WeatherDataCollector
