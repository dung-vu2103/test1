package com.ringme.cms.common;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Log4j2
@Component
public class ExportExcel {

    public void export(List<?> objects, String[] headers, HttpServletResponse response, String date, String title){
        try {
                long startTime = System.currentTimeMillis();
                writeExcel(objects, headers, response, date, title);
                long endTime = System.currentTimeMillis();
                long timeExport = endTime - startTime;
                log.info("Time Export|" + timeExport);
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }

    private static void writeExcel(List<?> objects, String[] headers, HttpServletResponse response, String date, String title) {
        try {
            // Create Workbook
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            // Create sheet
            SXSSFSheet sheet = workbook.createSheet("ReportSDK"); // Create sheet with sheet name
            int rowIndex = 6;
            int rowTitle = 0;

            //Viet Title
            Row titleTitle = sheet.createRow(rowTitle);
            Cell titleCell = titleTitle.createCell(0);
            titleCell.setCellValue(title);
            titleCell.setCellStyle(createTitleStyle(workbook));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6)); // Gộp từ cột 0 đến cột 6

            //Viet Date
            // Điền nội dung vào hàng 3, cột 2
            int contentRowIndex = 2; // Hàng 3 (chỉ số bắt đầu từ 0)
            int contentColumnIndex = 1; // Cột 2 (chỉ số bắt đầu từ 0)
            SXSSFRow contentRow = sheet.getRow(contentRowIndex); // Lấy dòng cần điền nội dung
            if (contentRow == null) {
                contentRow = sheet.createRow(contentRowIndex);
            }
            SXSSFCell contentCell = contentRow.createCell(contentColumnIndex); // Tạo ô cần điền nội dung
            contentCell.setCellValue("Date:"); // Điền nội dung vào ô
            contentCell.setCellStyle(createCellStyle(workbook));

            //Gia trị Date
            contentRow = sheet.getRow(contentRowIndex); // Lấy dòng cần điền nội dung
            if (contentRow == null) {
                contentRow = sheet.createRow(contentRowIndex);
            }
            contentCell = contentRow.createCell(contentColumnIndex + 1); // Tạo ô cần điền nội dung
            contentCell.setCellValue(date); // Điền nội dung vào ô
            contentCell.setCellStyle(createCellDataStyle(workbook));

            //Viet Total
            // Điền nội dung vào hàng 4, cột 2
            contentRow = sheet.getRow(contentRowIndex + 1); // Lấy dòng cần điền nội dung
            if (contentRow == null) {
                contentRow = sheet.createRow(contentRowIndex + 1);
            }
            contentCell = contentRow.createCell(contentColumnIndex); // Tạo ô cần điền nội dung
            contentCell.setCellValue("Total:"); // Điền nội dung vào ô
            contentCell.setCellStyle(createCellStyle(workbook));

            //Dien so luong gia tri
            contentRow = sheet.getRow(contentRowIndex + 1); // Lấy dòng cần điền nội dung
            if (contentRow == null) {
                contentRow = sheet.createRow(contentRowIndex + 1);
            }
            contentCell = contentRow.createCell(contentColumnIndex + 1); // Tạo ô cần điền nội dung
            contentCell.setCellValue(objects.size()); // Điền nội dung vào ô
            contentCell.setCellStyle(createCellDataStyle(workbook));

            // Write header
            writeHeader(sheet, rowIndex, headers, workbook);

            // Write data
            rowIndex++;
            for (Object object : objects) {
                // Create row
                SXSSFRow row = sheet.createRow(rowIndex);
                // Write data on row
                writeObject(object, row, workbook);
                rowIndex++;
            }
            // Create file excel
            createOutputFile(workbook, response);
            log.info("Done!!!");
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }

    private static CellStyle createTitleStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex()); // Đặt màu chữ là đen
        font.setFontHeightInPoints((short) 20); // Đặt kích thước chữ
        font.setFontName("Times New Roman");
        style.setAlignment(HorizontalAlignment.CENTER); // Canh giữa văn bản
        style.setFont(font);
        return style;
    }

    private static CellStyle createCellStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex()); // Đặt màu chữ là đen
        font.setFontHeightInPoints((short) 16); // Đặt kích thước chữ
        font.setFontName("Times New Roman");
        style.setAlignment(HorizontalAlignment.CENTER); // Canh giữa văn bản
        style.setFont(font);
        return style;
    }

    private static CellStyle createCellDataStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex()); // Đặt màu chữ là đen
        font.setFontHeightInPoints((short) 12); // Đặt kích thước chữ
        font.setFontName("Times New Roman");
        style.setFont(font);
        return style;
    }

    private static CellStyle createCellRowTitltTableStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex()); // Đặt màu chữ là đen
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // Đặt màu nền
        font.setFontHeightInPoints((short) 12); // Đặt kích thước chữ
        font.setFontName("Times New Roman");
        style.setFont(font);
        return style;
    }

    // Write header with format
    private static void writeHeader(SXSSFSheet sheet, int rowIndex, String[] headers, SXSSFWorkbook workbook) {
        // Create row
        SXSSFRow row = sheet.createRow(rowIndex);
        // Create cells
        SXSSFCell cell;

        CellStyle headerStyle = createHeaderStyle(workbook);
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(createCellRowTitltTableStyle(workbook));
            sheet.setColumnWidth(i, 15 * 256);
        }
    }

    private static CellStyle createHeaderStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex()); // Đặt màu chữ
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // Đặt màu nền
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);
        return style;
    }

    // Write data
    private static void writeObject(Object object, SXSSFRow row, SXSSFWorkbook workbook) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                field.setAccessible(true);
                Object value = field.get(object);

                SXSSFCell cell = row.createCell(i);
                cell.setCellValue(value != null ? value.toString() : "");
                cell.setCellStyle(createCellDataStyle(workbook));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // Create output file
    private static void createOutputFile(SXSSFWorkbook workbook, HttpServletResponse response) {
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();

            outputStream.close();
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }

    public HttpServletResponse setResponse(HttpServletResponse response){
        try {
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=game-starts-" + currentDateTime + ".xlsx";
            response.setHeader(headerKey, headerValue);
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return response;
    }

    // Write header SDK
    public static void writeHeaderCustom(SXSSFSheet sheet, int rowIndex, String[] headers) {
        // Create row
        SXSSFRow row = sheet.createRow(rowIndex);
        // Create cells
        SXSSFCell cell;
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            if (i == 0) cell.setCellValue("Mã BĐT");
            if (i == 1) cell.setCellValue("Tên BĐT");
            if (i == 2) cell.setCellValue("Mã Bưu Cục");
            if (i == 3) cell.setCellValue("Số liệu đầu KỲ báo cáo");
            if (i == 30) cell.setCellValue("SỐ LIỆU PHÁT SINH MỚI TRONG KỲ BÁO CÁO");
            if (i == 57) cell.setCellValue("SỐ LIỆU LUỸ KẾ CUỐI KỲ BÁO CÁO");
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 29));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 30, 56));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 57, 83));
        row = sheet.createRow(rowIndex + 1);
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            if (i == 3) cell.setCellValue("Số lượng account đã mở");
            if (i == 9) cell.setCellValue("Số lượng account phát sinh chat");
            if (i == 16) cell.setCellValue("Chat KH vs Bưu tá (DD)");
            if (i == 18) cell.setCellValue("SL phiên chat KH với bưu cục (PNS)");
            if (i == 24) cell.setCellValue("SL phiên Chat KH với đầu mối CSKH (One CX)");
            if (i == 27) cell.setCellValue("SL phiên Chat KH với đầu mối bán hàng (CCP)");
            if (i == 30) cell.setCellValue("Số lượng account đã mở");
            if (i == 36) cell.setCellValue("Số lượng account phát sinh chat");
            if (i == 43) cell.setCellValue("Chat KH vs Bưu tá (DD)");
            if (i == 45) cell.setCellValue("SL phiên chat KH với bưu cục (PNS)");
            if (i == 51) cell.setCellValue("SL phiên Chat KH với đầu mối CSKH (One CX)");
            if (i == 54) cell.setCellValue("SL phiên Chat KH với đầu mối bán hàng (CCP)");
            if (i == 57) cell.setCellValue("Số lượng account đã mở");
            if (i == 63) cell.setCellValue("Số lượng account phát sinh chat");
            if (i == 70) cell.setCellValue("Chat KH vs Bưu tá (DD)");
            if (i == 73) cell.setCellValue("SL phiên chat KH với bưu cục (PNS)");
            if (i == 79) cell.setCellValue("SL phiên Chat KH với đầu mối CSKH (One CX)");
            if (i == 82) cell.setCellValue("SL phiên Chat KH với đầu mối bán hàng (CCP)");
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 8));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 9, 15));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 16, 17));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 18, 23));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 24, 26));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 27, 29));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 30, 35));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 36, 42));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 43, 44));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 45, 50));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 51, 53));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 54, 56));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 57, 62));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 63, 69));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 70, 72));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 73, 78));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 79, 81));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 82, 83));
    }
}