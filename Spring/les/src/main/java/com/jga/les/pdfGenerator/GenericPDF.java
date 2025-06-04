package com.jga.les.pdfGenerator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jga.les.dtos.relatorios.PdfFormat;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class GenericPDF {

    public static byte[] gerarRelatorioBytes(List<?> objetos, String nomeRelatorio) {
        Document doc = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(doc, byteArrayOutputStream);
            doc.open();
            doc.add(new Paragraph(nomeRelatorio + "\n\n",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));

            if (objetos == null || objetos.isEmpty()) {
                doc.add(new Paragraph("Nenhum dado encontrado."));
                doc.close();
                return byteArrayOutputStream.toByteArray();
            }

            Class<?> classeDoObjeto = objetos.getFirst().getClass();
            Field[] campos = classeDoObjeto.getDeclaredFields();

            PdfPTable tabela = new PdfPTable(campos.length);
            tabela.setWidthPercentage(100);

            // Cabeçalhos
            for (Field campo : campos) {
                PdfPCell cabecalho = new PdfPCell(new Phrase(campo.getName().toUpperCase()));
                cabecalho.setBackgroundColor(BaseColor.LIGHT_GRAY);
                tabela.addCell(cabecalho);
            }

            // Linhas com dados
            for (Object obj : objetos) {
                for (Field campo : campos) {
                    campo.setAccessible(true);
                    Object valor = campo.get(obj);
                    tabela.addCell(formatarValor(campo, valor));
                }
            }

            doc.add(tabela);
            doc.close();
            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        } finally {
            try {
                if (doc.isOpen()) doc.close();
                byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String formatarValor(Field campo, Object valor) {
        if (valor == null) {
            PdfFormat annotation = campo.getAnnotation(PdfFormat.class);
            return annotation != null ? annotation.nullValue() : "N/A";
        }

        PdfFormat format = campo.getAnnotation(PdfFormat.class);

        try {
            if (valor instanceof LocalDate && format != null) {
                return ((LocalDate) valor).format(DateTimeFormatter.ofPattern(format.datePattern()));
            } else if (valor instanceof LocalDateTime && format != null) {
                return ((LocalDateTime) valor).format(DateTimeFormatter.ofPattern(format.dateTimePattern()));
            } else if (valor instanceof Number && format != null) {
                DecimalFormat df = new DecimalFormat(format.numberPattern());
                return df.format(valor);
            }
            return valor.toString();
        } catch (Exception e) {
            return valor.toString(); // Fallback para toString() se houver erro na formatação
        }
    }

    public static byte[] gerarRelatorioImagem(byte[] imagemBytes, String nomeRelatorio) {
        Document doc = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(doc, byteArrayOutputStream);
            doc.open();

            // Título
            doc.add(new Paragraph(nomeRelatorio,
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            doc.add(Chunk.NEWLINE);

            // Adiciona a imagem ao PDF
            Image img = Image.getInstance(imagemBytes);
            img.scaleToFit(500, 500); // redimensiona se necessário
            img.setAlignment(Image.ALIGN_CENTER);
            doc.add(img);

            doc.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        } finally {
            try {
                if (doc.isOpen()) doc.close();
                byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}