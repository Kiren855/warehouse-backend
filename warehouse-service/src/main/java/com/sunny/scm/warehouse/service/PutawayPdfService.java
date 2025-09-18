package com.sunny.scm.warehouse.service;

import com.sunny.scm.warehouse.dto.suggest.PutawaySuggestionDto;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PutawayPdfService {

    public byte[] generatePutawayPdf(Long putawayGroupId, List<PutawaySuggestionDto> suggestions) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font normalFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                float margin = 50;
                float yOffset = 750;

                // --- Header and Title ---
                contentStream.beginText();
                contentStream.setFont(boldFont, 18);
                contentStream.newLineAtOffset(margin, yOffset);
                contentStream.showText("Putaway Instructions Sheet");
                contentStream.endText();

                yOffset -= 30;
                contentStream.beginText();
                contentStream.setFont(normalFont, 12);
                contentStream.newLineAtOffset(margin, yOffset);
                contentStream.showText("Group Receipt ID: " + putawayGroupId);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Date: " + java.time.LocalDate.now());
                contentStream.endText();

                yOffset -= 40;

                // --- Putaway Suggestions ---
                contentStream.beginText();
                contentStream.setFont(boldFont, 14);
                contentStream.newLineAtOffset(margin, yOffset);
                contentStream.showText("Putaway Suggestions:");
                contentStream.endText();

                yOffset -= 20;

                // Group suggestions by zone
                Map<String, Map<String, List<PutawaySuggestionDto>>> groupedByZoneAndBin = suggestions.stream()
                        .collect(Collectors.groupingBy(
                                PutawaySuggestionDto::getSuggestedZoneCode,
                                Collectors.groupingBy(PutawaySuggestionDto::getSuggestedBinCode)
                        ));

                for (Map.Entry<String, Map<String, List<PutawaySuggestionDto>>> zoneEntry : groupedByZoneAndBin.entrySet()) {
                    yOffset -= 20;
                    // Draw a section for each Zone
                    String zoneName = zoneEntry.getValue().values().stream().findFirst().get().get(0).getSuggestedZoneName();
                    contentStream.beginText();
                    contentStream.setFont(boldFont, 12);
                    contentStream.newLineAtOffset(margin + 10, yOffset);
                    contentStream.showText("Zone: " + zoneEntry.getKey() + " (" + zoneName + ")");
                    contentStream.endText();

                    yOffset -= 15;
                    for (Map.Entry<String, List<PutawaySuggestionDto>> binEntry : zoneEntry.getValue().entrySet()) {
                        yOffset -= 15;
                        // Draw a subsection for each Bin
                        String binName = binEntry.getValue().get(0).getSuggestedBinName();
                        contentStream.beginText();
                        contentStream.setFont(boldFont, 10);
                        contentStream.newLineAtOffset(margin + 20, yOffset);
                        contentStream.showText("Bin: " + binEntry.getKey() + " (" + binName + ")");
                        contentStream.endText();

                        yOffset -= 20;

                        // Create a table-like structure for the items
                        float tableY = yOffset;
                        float[] colWidths = {150, 100, 70, 100};

                        // Table Header
                        String[] headers = {"Product Name (SKU)", "Barcode", "Quantity", "Putaway Date"};

                        contentStream.beginText();
                        contentStream.setFont(boldFont, 8);
                        contentStream.newLineAtOffset(margin + 30, tableY);
                        for (int i = 0; i < headers.length; i++) {
                            contentStream.showText(headers[i]);
                            contentStream.newLineAtOffset(colWidths[i], 0);
                        }
                        contentStream.endText();

                        tableY -= 15;

                        // Table Rows
                        for (PutawaySuggestionDto item : binEntry.getValue()) {
                            contentStream.beginText();
                            contentStream.setFont(normalFont, 8);
                            contentStream.newLineAtOffset(margin + 30, tableY);
                            contentStream.showText(item.getProductName() + " (" + item.getProductSku() + ")");
                            contentStream.newLineAtOffset(colWidths[0], 0);
                            contentStream.showText(item.getPackageBarcode());
                            contentStream.newLineAtOffset(colWidths[1], 0);
                            contentStream.showText(String.valueOf(item.getQuantityToPutaway()));
                            contentStream.newLineAtOffset(colWidths[2], 0);
                            contentStream.showText("________________"); // Placeholder for signature/date
                            contentStream.endText();
                            tableY -= 15;

                            if (tableY < margin) {
                                // Add a new page if the content exceeds the current page
                                contentStream.close();
                                page = new PDPage(PDRectangle.A4);
                                document.addPage(page);
                                contentStream.beginText();
                                yOffset = 750;
                                tableY = yOffset;
                            }
                        }
                        yOffset = tableY; // Update yOffset after the table
                    }
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }
}