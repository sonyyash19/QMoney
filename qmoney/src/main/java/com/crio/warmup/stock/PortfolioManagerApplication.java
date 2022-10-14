
package com.crio.warmup.stock;


import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
// import java.util.Map;
// import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;


public class PortfolioManagerApplication {

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Task:
  //       - Read the json file provided in the argument[0], The file is available in the classpath.
  //       - Go through all of the trades in the given file,
  //       - Prepare the list of all symbols a portfolio has.
  //       - if "trades.json" has trades like
  //         [{ "symbol": "MSFT"}, { "symbol": "AAPL"}, { "symbol": "GOOGL"}]
  //         Then you should return ["MSFT", "AAPL", "GOOGL"]
  //  Hints:
  //    1. Go through two functions provided - #resolveFileFromResources() and #getObjectMapper
  //       Check if they are of any help to you.
  //    2. Return the list of all symbols in the same order as provided in json.

  //  Note:
  //  1. There can be few unused imports, you will need to fix them to make the build pass.
  //  2. You can use "./gradlew build" to check if your code builds successfully.

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {

    // Trades[] trades = om.readValue(inputFile, Trade[].class);

    List<String> list = new ArrayList<>();
    PortfolioTrade[] trades = getObjectMapper().readValue(resolveFileFromResources(args[0]), PortfolioTrade[].class);

    if(trades.length == 0){
      return new ArrayList<>();
    }

    for(PortfolioTrade trade: trades){
      System.out.println(trade.getSymbol());
      list.add(trade.getSymbol());
    }
     return list;
  }


  // Note:
  // 1. You may needto copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.









  // TODO: CRIO_TASK_MODULE_REST_API
  //  Find out the closing price of each stock on the end_date and return the list
  //  of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  //    and deserialize the results in List<Candle>



  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }


  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Follow the instructions provided in the task documentation and fill up the correct values for
  //  the variables provided. First value is provided for your reference.
  //  A. Put a breakpoint on the first line inside mainReadFile() which says
  //    return Collections.emptyList();
  //  B. Then Debug the test #mainReadFile provided in PortfoliomanagerApplicationTest.java
  //  following the instructions to run the test.
  //  Once you are able to run the test, perform following tasks and record the output as a
  //  String in the function below.
  //  Use this link to see how to evaluate expressions -
  //  https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  //  1. evaluate the value of "args[0]" and set the value
  //     to the variable named valueOfArgument0 (This is implemented for your reference.)
  //  2. In the same window, evaluate the value of expression below and set it
  //  to resultOfResolveFilePathArgs0
  //     expression ==> resolveFileFromResources(args[0])
  //  3. In the same window, evaluate the value of expression below and set it
  //  to toStringOfObjectMapper.
  //  You might see some garbage numbers in the output. Dont worry, its expected.
  //    expression ==> getObjectMapper().toString()
  //  4. Now Go to the debug window and open stack trace. Put the name of the function you see at
  //  second place from top to variable functionNameFromTestFileInStackTrace
  //  5. In the same window, you will see the line number of the function in the stack trace window.
  //  assign the same to lineNumberFromTestFileInStackTrace
  //  Once you are done with above, just run the corresponding test and
  //  make sure its working as expected. use below command to do the same.
  //  ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs() {

    String valueOfArgument0 = "trades.json";
    String resultOfResolveFilePathArgs0 = "/opt/criodo/qmoney/me_qmoney/qmoney/"
        + "build/resources/main/trades.json";
    String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@7350471";
    String functionNameFromTestFileInStackTrace = "mainReadFile";
    String lineNumberFromTestFileInStackTrace = "29";




    return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
        toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
        lineNumberFromTestFileInStackTrace});
  }


  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.
  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {

    List<PortfolioTrade> trades = readTradesFromJson(args[0]);
    List<TotalReturnsDto> totalReturns = new ArrayList<>();
    RestTemplate restTemplate = new RestTemplate();
    for(PortfolioTrade trade: trades){
      String symbol = trade.getSymbol();
      LocalDate endDate = LocalDate.parse(args[1]);
      String url = prepareUrl(trade, endDate, "717f1e94ec7d91f4a610309c3dd9d2d22f741dca");
      System.out.println("URL " + url);
      TiingoCandle[] tingoApi = restTemplate.getForObject(url, TiingoCandle[].class);
      if(tingoApi == null) continue;
      TotalReturnsDto temp = new TotalReturnsDto(symbol, tingoApi[tingoApi.length-1].getClose());
      totalReturns.add(temp);
    }

    Collections.sort(totalReturns, new Comparator<TotalReturnsDto>() {
      @Override
      public int compare(TotalReturnsDto t1, TotalReturnsDto t2){
        return (int)(t1.getClosingPrice().compareTo(t2.getClosingPrice()));
      }
    });

    System.out.println(totalReturns.toString());
    List<String> result = new ArrayList<>();
    for(TotalReturnsDto total: totalReturns){
      
      result.add(total.getSymbol());
    }

    return result;
     
  }

  

  // TODO:
  //  After refactor, make sure that the tests pass by using these two commands
  //  ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  //  ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile
  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException {
    List<PortfolioTrade> list = new ArrayList<>();
    PortfolioTrade[] trades = getObjectMapper().readValue(resolveFileFromResources(filename), PortfolioTrade[].class);

    if(trades.length == 0){
      return new ArrayList<>();
    }

    for(PortfolioTrade trade: trades){
      list.add(trade);
    }
     return list;
  }
  


  // TODO:
  //  Build the Url using given parameters and use this function in your code to cann the API.
  // public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
  //   String url = "https://api.tiingo.com/tiingo/daily/" + trade.getSymbol() + "/prices?token=" + token  + "&endDate=" + endDate + "&resampleFreq=monthly";
  //    return url;
  // }
  
public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
    return "https://api.tiingo.com/tiingo/daily/" + trade.getSymbol() + "/prices?startDate=" + trade.getPurchaseDate()
        + "&endDate=" + endDate + "&token=" + token;
  }


//  String url = "&startDate=2016-1-1&endDate=2016-1-1&resampleFreq=monthly";



  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Now that you have the list of PortfolioTrade and their data, calculate annualized returns
  //  for the stocks provided in the Json.
  //  Use the function you just wrote #calculateAnnualizedReturns.
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.

  public static String getToken(){
    return "717f1e94ec7d91f4a610309c3dd9d2d22f741dca";
  }



  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {

    return candles.get(0).getOpen();
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
     return candles.get(candles.size() - 1).getClose();
  }


  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
          RestTemplate restTemplate = new RestTemplate();
          String url = prepareUrl(trade, endDate, token);
          TiingoCandle[] tingoApi = restTemplate.getForObject(url, TiingoCandle[].class);
          List<Candle> candles = Arrays.asList(tingoApi);
     return candles;
  }

   // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Now that you have the list of PortfolioTrade and their data, calculate annualized returns
  //  Use the function you just wrote #calculateAnnualizedReturns.
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {

        List<PortfolioTrade> trades = readTradesFromJson(args[0]); //list of trades

        List<AnnualizedReturn> annualizedReturns = new ArrayList<>();

        for(PortfolioTrade trade: trades){
          LocalDate endDate = LocalDate.parse(args[1]);
          
          List<Candle> candles = fetchCandles(trade, endDate, getToken());

          double openingPriceOnPurchaseDate = getOpeningPriceOnStartDate(candles);
          double closingPrice = getClosingPriceOnEndDate(candles);

          annualizedReturns.add(calculateAnnualizedReturns(endDate, trade, openingPriceOnPurchaseDate, closingPrice));
        
    }

     return sortAnnialAnnualizedReturnsInDescendingorder(annualizedReturns);
  }

  public static List<AnnualizedReturn> sortAnnialAnnualizedReturnsInDescendingorder(List<AnnualizedReturn> annualizedReturns){
    return annualizedReturns.stream().sorted((o1, o2) -> o2.getAnnualizedReturn().compareTo(o1.getAnnualizedReturn())).collect(Collectors.toList());
  }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {

      // double totalNumberYears = trade.getPurchaseDate().until(endDate, ChronoUnit.DAYS)/365.24; //alternate way to calculate the year

      double totalNumberYears = ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate) / 365.2422;
      double totalReturns = (sellPrice - buyPrice) / buyPrice; //absoluteReturn

      double annualizedReturns = Math.pow((1.0 + totalReturns), (1.0 / totalNumberYears)) - 1;

      return new AnnualizedReturn(trade.getSymbol(), annualizedReturns, totalReturns);
  }

  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());

    // printJsonObject(mainReadFile(args));

    // printJsonObject(mainReadQuotes(args));

    printJsonObject(mainCalculateSingleReturn(args));

  }
}

