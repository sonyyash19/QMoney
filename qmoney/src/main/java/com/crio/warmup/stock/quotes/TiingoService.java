
package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class TiingoService implements StockQuotesService {
  
  private RestTemplate restTemplate;

  protected TiingoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

   // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement getStockQuote method below that was also declared in the interface.

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
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.registerModule(new JavaTimeModule());

        Candle[] candleObj = null;
        String url = buildUri(symbol, from, to);
        String tingoCandle = restTemplate.getForObject(url, String.class);
        try {
          candleObj = objMapper.readValue(tingoCandle, TiingoCandle[].class);
        } catch (JsonMappingException e) {
          e.printStackTrace();
        } catch(JsonProcessingException e){
          e.printStackTrace();
        }

        if(candleObj == null){
          return new ArrayList<>();
        }

     return Arrays.asList(candleObj);
  }

  // private List<Candle> sortCandleInAscendingOrderBasedOnDate(List<Candle> candle){
  //     return candle.stream().sorted((c1,c2) -> c1.getDate().compareTo(c2.getDate())).collect(Collectors.toList());
  // }

  // Note:
  // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
  // 2. Run the tests using command below and make sure it passes.
  //    ./gradlew test --tests TiingoServiceTest


  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Write a method to create appropriate url to call the Tiingo API.

}