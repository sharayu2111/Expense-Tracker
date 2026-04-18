package com.expensetracker.util;

import com.expensetracker.model.Transaction;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

// Demonstrating File Handling
public class FileExporter {
    
    public static void exportTransactionsToCSV(List<Transaction> list, String filePath) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("ID,Type,Amount,Category,Date,Description\n");
            for (Transaction t : list) {
                writer.write(String.format("%d,%s,%.2f,%s,%s,%s\n",
                    t.getId(),
                    t.getTransactionType(),
                    t.getAmount(),
                    t.getCategory(),
                    df.format(t.getDate()),
                    t.getDescription() != null ? t.getDescription() : ""
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
