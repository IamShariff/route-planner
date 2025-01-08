package com.metro.routeplanner.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.metro.routeplanner.exception.NotFoundException;
import com.metro.routeplanner.exception.QRCodeGenerationException;
import com.metro.routeplanner.model.Ticket;
import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class QRCodeHelper {

    public static byte[] generateQRCode(String text, int width, int height) throws IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            throw new QRCodeGenerationException(Constant.QR_CODE_GENERATION_FAILED + e.getMessage());
        }

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public static String decodeQRCode(InputStream qrCodeImage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeImage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result;
        try {
            result = new MultiFormatReader().decode(bitmap);
        } catch (com.google.zxing.NotFoundException e) {
            throw new NotFoundException("QR Code", Constant.QR_CODE_NOT_FOUND);
        }
        return result.getText();
    }

    public static String generateQRData(Ticket ticket) {
        return String.format(Constant.QR_DATA_FORMAT,
                ticket.getId(),
                ticket.getUserId(), ticket.getSourceStation(), ticket.getDestinationStation(),
                ticket.getPaymentMethod(), ticket.getCreatedAt(), ticket.getExpiryTime(), ticket.getFare(),
                ticket.isUsedAtSource(), ticket.isUsedAtDestination());
    }

    // Helper method to parse QR data (assuming data is stored as key-value pairs)
    public static Map<String, String> parseQRData(String qrData) {
        return Arrays.stream(qrData.split(Constant.QR_DATA_DELIMITER))
                .map(entry -> entry.split(Constant.QR_KEY_VALUE_SEPARATOR))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
    }

    // Extract data from QR code
    public static String extractDataFromQR(MultipartFile file) throws IOException {
        return QRCodeHelper.decodeQRCode(file.getInputStream()); // Decoding QR code logic
    }
}