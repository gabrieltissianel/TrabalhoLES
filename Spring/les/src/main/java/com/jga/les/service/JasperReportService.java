package com.jga.les.service;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class JasperReportService {
    public <T> byte[] getRelatorio(List<T> items, String fileName, String title) throws JRException, IOException {
        String jrxmlPath = "/home/pompom/Desktop/Faculdade/periodo7/LES/TrabalhoLES/Spring/les/src/main/resources/report/"+fileName+".jrxml";
        String jasperOutputPath = "/home/pompom/Desktop/Faculdade/periodo7/LES/TrabalhoLES/Spring/les/src/main/resources/report/"+fileName+".jasper";

        JasperCompileManager.compileReportToFile(jrxmlPath, jasperOutputPath);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);

        //Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperOutputPath, parameters, dataSource);
        byte[] reportContent = JasperExportManager.exportReportToPdf(jasperPrint);

        return reportContent;
    }
}
