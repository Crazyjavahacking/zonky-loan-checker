package cz.zonky.loans.marketplace;

public class MarketplaceConnectionException extends RuntimeException {
    public MarketplaceConnectionException(String format) {
        super(format);
    }
}
