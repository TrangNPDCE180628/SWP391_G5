/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Ultis;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletContext;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtils {

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static class InventoryData {
        String productId;
        String productName;
        int beginningInventory = 0;
        int imported = 0;
        int exported = 0;
        boolean createdInPeriod = false;
        boolean deletedInPeriod = false;

        int getFinalInventory() {
            return beginningInventory + imported - exported;
        }
    }

    public static File generateInventoryReport(ServletContext context, Date startDate, Date endDate, String logFileName) throws IOException, ParseException {
        String relativePath = "/logs/" + logFileName;
        String absolutePath = context.getRealPath(relativePath);
        File logFile = new File(absolutePath);

        if (!logFile.exists()) {
            throw new FileNotFoundException("Log file not found: " + absolutePath);
        }

        Map<String, InventoryData> inventoryMap = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Format: [2025-07-20 10:12:34] [Beginning Inventory] SP001 | Laptop Dell XPS | 10
                String[] parts = line.split("] ");
                if (parts.length < 2) continue;

                String timestampStr = parts[0].substring(1);
                Date logDate = dateTimeFormat.parse(timestampStr);
                if (logDate.before(startDate) && !parts[1].contains("Beginning Inventory")) continue;

                if (logDate.after(endDate)) continue;

                String[] details = parts[1].split(" ", 2);
                if (details.length < 2) continue;

                String action = details[0].replace("[", "").replace("]", "").trim();
                String[] values = details[1].split("\\|");
                if (values.length < 3) continue;

                String productId = values[0].trim();
                String productName = values[1].trim();
                int quantity = Integer.parseInt(values[2].trim());

                InventoryData data = inventoryMap.getOrDefault(productId, new InventoryData());
                data.productId = productId;
                data.productName = productName;

                switch (action) {
                    case "Beginning Inventory":
                        if (dateOnlyFormat.format(logDate).equals(dateOnlyFormat.format(startDate))) {
                            data.beginningInventory = quantity;
                        }
                        break;
                    case "Imported":
                        data.imported += quantity;
                        break;
                    case "Exported":
                        data.exported += quantity;
                        break;
                    case "Created":
                        if (!logDate.before(startDate) && !logDate.after(endDate)) {
                            data.createdInPeriod = true;
                            data.beginningInventory = 0; // Default when just created
                        }
                        break;
                    case "Deleted":
                        if (!logDate.before(startDate) && !logDate.after(endDate)) {
                            data.deletedInPeriod = true;
                        }
                        break;
                }

                inventoryMap.put(productId, data);
            }
        }

        // Tạo Excel Workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Báo cáo tồn kho");

        // Header
        Row header = sheet.createRow(0);
        String[] columns = {"Mã SP", "Tên sản phẩm", "Tồn đầu kỳ", "Nhập trong kỳ", "Xuất trong kỳ", "Tồn cuối kỳ"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Data Rows
        int rowIndex = 1;
        for (InventoryData data : inventoryMap.values()) {
            if (data.deletedInPeriod) continue; // Bỏ qua sản phẩm bị xóa trong kỳ

            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(data.productId);
            row.createCell(1).setCellValue(data.productName);
            row.createCell(2).setCellValue(data.beginningInventory);
            row.createCell(3).setCellValue(data.imported);
            row.createCell(4).setCellValue(data.exported);
            row.createCell(5).setCellValue(data.getFinalInventory());
        }

        // Auto-size columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to temp file
        File tempFile = File.createTempFile("inventory_report_", ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            workbook.write(fos);
        }

        workbook.close();
        return tempFile;
    }
}
