package com.example.rp.controller;

import com.example.rp.model.Receipt;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

@RestController
class RPRestController {

    private static final String BAD_REQUEST = "The receipt is invalid.";
    private static final String NOT_FOUND = "No receipt found for that ID.";

    private final HashMap<String, Long> receiptIdToPointsMap = new HashMap<>();

    // /receipts/process
    // Returns the ID assigned to the receipt
    // { "id": "7fb1377b-b223-49d9-a31a-5a02701dd310" }
    @PostMapping("/receipts/process")
    public String process(@RequestBody Receipt receipt) {
        if (receipt.hasRequiredItems()) {
            receiptIdToPointsMap.put(receipt.getReceiptId(), receipt.getPointValue());
            return "{ \"id\": \"" + receipt.getReceiptId() + "\" }";
        }
        return BAD_REQUEST;
    }

    // /receipts/{id}/points
    // Returns the points awarded for the receipt
    // { "points": 32 }
    @GetMapping("/receipts/{id}/points")
    public String getPoints(@PathVariable String id) {
        if (receiptIdToPointsMap.containsKey(id)) {
            return "{ \"points\": \"" + receiptIdToPointsMap.get(id) + "\" }";
        }
        return NOT_FOUND;
    }
}
