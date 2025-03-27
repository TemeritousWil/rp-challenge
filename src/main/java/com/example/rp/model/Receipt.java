package com.example.rp.model;

/*
{
    "retailer": "Walgreens",
    "purchaseDate": "2022-01-02",
    "purchaseTime": "08:13",
    "total": "2.65",
    "items": [
        {"shortDescription": "Pepsi - 12-oz", "price": "1.25"},
        {"shortDescription": "Dasani", "price": "1.40"}
    ]
}
 */

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static java.util.UUID.randomUUID;

public class Receipt {

    private static final LocalTime startTime = LocalTime.of(14, 0);
    private static final LocalTime endTime = LocalTime.of(16, 0);

    private String retailer;
    private LocalDate purchaseDate;
    private LocalTime purchaseTime;
    private BigDecimal total;
    private List<Item> items;

    private final String receiptId = randomUUID().toString();
    public boolean hasRequiredItems() {
        return retailer != null && !retailer.isEmpty() &&
                purchaseDate != null && purchaseTime != null &&
                total != null && !items.isEmpty();
    }

    public String getReceiptId() {
        return receiptId;
    }

    /*
        One point for every alphanumeric character in the retailer name.
        50 points if the total is a round dollar amount with no cents.
        25 points if the total is a multiple of 0.25.
        5 points for every two items on the receipt.
        If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer. The result is the number of points earned.
        6 points if the day in the purchase date is odd.
        10 points if the time of purchase is after 2:00pm and before 4:00pm.
    */
    public long getPointValue() {
        return pointsRuleName() +
                pointsRuleRoundDollar() +
                pointsRuleRoundQuarter() +
                pointsRulePairedItems() +
                pointsRuleDescriptionMultipleOfThree() +
                pointsRulePurchaseDateOdd() +
                pointsRulePurchaseTimeMidAfternoon();
    }

    // One point for every alphanumeric character in the retailer name.
    long pointsRuleName() {
        long totalPoints = 0;
        final String retailer = getRetailer();
        for (int ch = 0; ch < retailer.length(); ch++) {
            if (Character.isLetterOrDigit(getRetailer().charAt(ch))) {
                totalPoints++;
            }
        }
        return totalPoints;
    }

    // 50 points if the total is a round dollar amount with no cents.
    long pointsRuleRoundDollar() {
        return (getTotal().stripTrailingZeros().scale() <= 0) ? 50 : 0;
    }

    // 25 points if the total is a multiple of 0.25.
    long pointsRuleRoundQuarter() {
        BigDecimal scaledAmount = getTotal().multiply(BigDecimal.valueOf(4));
        return (scaledAmount.stripTrailingZeros().scale() <= 0) ? 25 : 0;
    }

    // 5 points for every two items on the receipt.
    long pointsRulePairedItems() {
        return 5L * (Math.floorDiv(getItems().size(), 2));
    }

    // If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2
    // and round up to the nearest integer. The result is the number of points earned.
    long pointsRuleDescriptionMultipleOfThree() {
        long totalPoints = 0;
        for (Item item : getItems()) {
            if (item.getShortDescription().trim().length() % 3 == 0) {
                totalPoints += (long) Math.ceil(item.getPrice().multiply(BigDecimal.valueOf(0.2)).doubleValue());
            }
        }
        return totalPoints;
    }

    // 6 points if the day in the purchase date is odd.
    long pointsRulePurchaseDateOdd() {
        return (getPurchaseDate().getDayOfMonth() % 2 != 0) ? 6 : 0;
    }

    // 10 points if the time of purchase is after 2:00pm and before 4:00pm.
    long pointsRulePurchaseTimeMidAfternoon() {
        return (getPurchaseTime().isBefore(endTime) && getPurchaseTime().isAfter(startTime)) ? 10 : 0;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private String shortDescription;
        private BigDecimal price;

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}
