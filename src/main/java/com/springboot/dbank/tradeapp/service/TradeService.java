package com.springboot.dbank.tradeapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.dbank.tradeapp.dao.TradeStoreDao;
import com.springboot.dbank.tradeapp.dao.TradeRepository;
import com.springboot.dbank.tradeapp.model.Trade;

@Service
public class TradeService {

    private static final Logger log = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    TradeStoreDao tradeDao;

    @Autowired
    TradeRepository tradeRepository;
    
    public List<Trade> findAll(){
        return  tradeRepository.findAll();
     }

    public void  persist(Trade trade){
         trade.setCreatedDate(LocalDate.now());
         tradeRepository.save(trade);
     }
    
    public boolean isValidTrade(Trade trade){
        boolean result=false;
    	Optional<Trade> exsitingTrade = tradeRepository.findById(trade.getTradeId());
        if (exsitingTrade.isPresent()) {
        	result = validateVersion(trade, exsitingTrade.get());
        }
    	if(validateMaturityDate(trade)) {
    		result=true;
         }
         return result;
    }

    //2.	Store should not allow the trade which has less maturity date then today date
    private boolean validateMaturityDate(Trade trade){
        return trade.getMaturityDate().isBefore(LocalDate.now())  ? false:true;
    }
    
    private boolean validateVersion(Trade trade,Trade oldTrade) {
        //validation 1  During transmission if the
        // lower version is being received by the store it will reject the trade and throw an exception.
        if(trade.getVersion() >= oldTrade.getVersion()){
            return true;
        }
        return false;
    }
    
    public void updateExpiryFlagOfExistingTrade(){
    	
        tradeDao.tradeMap.forEach(
                (k,v) -> {
                    if(!validateMaturityDate(v)){
                        v.setExpiredFlag("N");
                        log.info("Trade which needs to updated {}",v);
                    }
                }
        );
         tradeRepository.findAll().stream().forEach(t -> {
                 if (!validateMaturityDate(t)) {
                     t.setExpiredFlag("Y");
                     log.info("Trade which needs to updated {}", t);
                     tradeRepository.save(t);
                 }
             });
         }
}
