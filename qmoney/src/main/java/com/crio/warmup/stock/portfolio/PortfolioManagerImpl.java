
package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {

  @Autowired
  private RestTemplate restTemplate;


  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  //TODO: CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
  //    Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  //    into #calculateAnnualizedReturn function here and ensure it follows the method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required further as our
  //    clients will take care of it, going forward.

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest

  //CHECKSTYLE:OFF




  private  Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Extract the logic to call Tiingo third-party APIs to a separate function.
  //  Remember to fill out the buildUri function and use that.


  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {
        String url = buildUri(symbol, from, to);
        TiingoCandle[] tingoCandle = restTemplate.getForObject(url, TiingoCandle[].class);
        List<Candle> candle = Arrays.asList(tingoCandle);
     return candle;
  }

  private static String getToken(){
    return "717f1e94ec7d91f4a610309c3dd9d2d22f741dca";
  }

  protected static String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    String token = getToken();
       String uriTemplate = "https://api.tiingo.com/tiingo/daily/" + symbol +
          "/prices?startDate= "+ startDate + "&endDate= "+ endDate + "&token=" + token;
            return uriTemplate;
  }

  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate) throws JsonProcessingException {

        // List<PortfolioTrade> trades = portfolioTrades; //list of trades

        List<AnnualizedReturn> annualizedReturns = new ArrayList<>();

        for(PortfolioTrade trade: portfolioTrades){
          List<Candle> candle = getStockQuote(trade.getSymbol(), trade.getPurchaseDate(), endDate);

          double openingPriceOnPurchaseDate = candle.get(0).getOpen();
          double closingPrice = candle.get(candle.size() - 1).getClose();

          annualizedReturns.add(calculateAnnualizedReturns(endDate, trade, openingPriceOnPurchaseDate, closingPrice));
        
    }
        
    return annualizedReturns.stream().sorted((o1, o2) -> o2.getAnnualizedReturn().compareTo(o1.getAnnualizedReturn())).collect(Collectors.toList());
  }

  private static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {

      // double totalNumberYears = trade.getPurchaseDate().until(endDate, ChronoUnit.DAYS)/365.24; //alternate way to calculate the year

      double totalNumberYears = ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate) / 365.2422;
      double totalReturns = (sellPrice - buyPrice) / buyPrice; //absoluteReturn

      double annualizedReturns = Math.pow((1.0 + totalReturns), (1.0 / totalNumberYears)) - 1;
      return new AnnualizedReturn(trade.getSymbol(), annualizedReturns, totalReturns);
  }
}
