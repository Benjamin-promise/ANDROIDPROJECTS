public class TradeCalculator {
    public static double calculateRisk(double balance, double lotSize, double leverage) {
        return balance * lotSize * leverage / 100;
    }

    public static double suggestTakeProfit(double entryPrice, double riskAmount) {
        return entryPrice + riskAmount * 2; // Example calculation
    }

    public static double suggestStopLoss(double entryPrice, double riskAmount) {
        return entryPrice - riskAmount; // Example calculation
    }
}
