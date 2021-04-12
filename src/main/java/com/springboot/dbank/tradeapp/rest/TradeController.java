package com.springboot.dbank.tradeapp.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.dbank.tradeapp.exception.InvalidTradeStoreException;
import com.springboot.dbank.tradeapp.model.Trade;
import com.springboot.dbank.tradeapp.service.TradeService;

@RestController
public class TradeController {
    @Autowired
    TradeService tradeService;
  
    
    @GetMapping("/")
    public String home() {

        return "Welcome in Trade Store Home page";
    }
    
    @GetMapping("/trade")
    public List<Trade> findAllTrades(){
        return tradeService.findAll();
    }
    
    @PostMapping("/trade")
    public ResponseEntity<String> tradeValidateStore(@RequestBody Trade trade){
       if(tradeService.isValidTrade(trade)) {
           tradeService.persist(trade);
       }else{
           throw new InvalidTradeStoreException(trade.getTradeId()+ "  Trade is not valid !!! Please check Maturity date or Version ");
    	 //  throw new RuntimeException(trade.getTradeId()+"  Trade Id is not found");
       }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
