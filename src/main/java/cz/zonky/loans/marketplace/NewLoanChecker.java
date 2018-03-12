package cz.zonky.loans.marketplace;

import java.util.List;
import cz.zonky.loans.marketplace.response.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;

@Service
public class NewLoanChecker {
    private static final Logger log = LoggerFactory.getLogger(NewLoanChecker.class);

    private LoanDownloader loanDownloader;
    private Calendar lastCheck = Calendar.getInstance();

    @Autowired
    public NewLoanChecker(LoanDownloader loanDownloader) {
        this.loanDownloader = loanDownloader;
        this.lastCheck.add(Calendar.DATE, -1); // initially set to previous day
    }

    /**
     * Prints new loans every 5 minutes
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void checkForNewLoans() {
        try {
            List<Loan> loans = loanDownloader.downloadLoans(lastCheck.getTime());
            lastCheck = Calendar.getInstance();

            if (loans.size() > 0) {
                log.info("New loans:");
                for (Loan loan : loans) {
                    log.info("New loan ID {} with title \"{}\"", loan.getId(), loan.getName());
                }
            } else {
                log.info("No new loans");
            }
        } catch (MarketplaceConnectionException e) {
            log.info(e.getMessage());
        }
    }
}
