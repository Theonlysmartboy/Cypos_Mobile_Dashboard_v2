package com.cybene.cyposdashboard.utils.interfaces;

import com.cybene.cyposdashboard.utils.data.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TransactionGenerator {
    public static List<Transaction> generateSampleTransactions(int count) {
        List<Transaction> transactions = new ArrayList<>();
        Random random = new Random();
        String[] statuses = {"Completed", "Pending", "Refunded"};

        for (int i = 0; i < count; i++) {
            boolean isSale = random.nextBoolean();
            String title = isSale ?
                    "Sale #" + (1000 + i) :
                    "Purchase #" + (200 + i);

            transactions.add(new Transaction(
                    "trans_" + i,
                    title,
                    100 + random.nextDouble() * 5000,
                    new Date(System.currentTimeMillis() - random.nextInt(86400000)),
                    isSale,
                    statuses[random.nextInt(statuses.length)]
            ));
        }
        return transactions;
    }
}
