
// package com.crio.warmup.stock.quotes;

// import com.crio.warmup.stock.dto.Candle;
// import com.crio.warmup.stock.dto.TiingoCandle;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import java.time.LocalDate;
// import java.util.Arrays;
// import java.util.List;
// import java.util.stream.Collectors;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.client.RestTemplate;

// public class TiingoService implements StockQuotesService {
  
//   private RestTemplate restTemplate;

//   protected TiingoService(RestTemplate restTemplate) {
//     this.restTemplate = restTemplate;
//     System.out.println(("Rest Template Inside constructor: " + restTemplate));
//   }

//    // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
//   //  Implement getStockQuote method below that was also declared in the interface.


//   @Override
//   public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
//       throws JsonProcessingException {
//         String url = buildUri(symbol, from, to);
//         System.out.println("URL=" + url);
//         TiingoCandle[] tingoCandle = restTemplate.getForObject(url, TiingoCandle[].class);
//         System.out.println("============");
//         System.out.println(tingoCandle);
//         System.out.println("=============");
//         List<Candle> candle = Arrays.asList(tingoCandle);
//         // candle = sortCandleInAscendingOrderBasedOnDate(candle);
//         System.out.println(candle.get(0).getOpen());
//      return candle;
//   }

//   private List<Candle> sortCandleInAscendingOrderBasedOnDate(List<Candle> candle){
//       return candle.stream().sorted((c1,c2) -> c1.getDate().compareTo(c2.getDate())).collect(Collectors.toList());
//   }

//   private static String getToken(){
//     return "717f1e94ec7d91f4a610309c3dd9d2d22f741dca";
//   }

//   protected static String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
//     String token = getToken();
//        String uriTemplate = "https://api.tiingo.com/tiingo/daily/" + symbol +
//           "/prices?startDate= "+ startDate + "&endDate= "+ endDate + "&token=" + token;
//             return uriTemplate;
//   }

//   // Note:
//   // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
//   // 2. Run the tests using command below and make sure it passes.
//   //    ./gradlew test --tests TiingoServiceTest


//   //CHECKSTYLE:OFF

//   // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
//   //  Write a method to create appropriate url to call the Tiingo API.

//   // public static void main(String[] args) throws JsonProcessingException {
//   //   TiingoService tingoService = new TiingoService(new RestTemplate());
//   //   tingoService.getStockQuote("GOOGL", LocalDate.parse("2019-01-01"), LocalDate.parse("2019-01-04"));
//   // }

// }

package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.client.RestTemplate;

public class TiingoService implements StockQuotesService {

  RestTemplate restTemplate=new RestTemplate();

  public String getToken(){
    return "717f1e94ec7d91f4a610309c3dd9d2d22f741dca";
  }

  protected TiingoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  protected String buildURL(String symbol, LocalDate startDate, LocalDate endDate) {

    String uriTemplate = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?" + "startDate="
        + startDate + "&endDate=" + endDate + "&token=" + getToken();
    return uriTemplate;
  }


  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      {
        ObjectMapper objmapper=new ObjectMapper();
        objmapper.registerModule(new JavaTimeModule());

        Candle[] candleobj=null;

        String apiresponse=restTemplate.getForObject(buildURL(symbol, from, to), String.class);

        try {
          candleobj=objmapper.readValue(apiresponse, TiingoCandle[].class);
        } catch (JsonMappingException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (JsonProcessingException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
        if(candleobj==null){
        
          return new ArrayList<>();
          
        }
  
        return Arrays.asList(candleobj);
        
  


      }

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement getStockQuote method below that was also declared in the interface.

  // Note:
  // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
  // 2. Run the tests using command below and make sure it passes.
  //    ./gradlew test --tests TiingoServiceTest


  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Write a method to create appropriate url to call the Tiingo API.

}
