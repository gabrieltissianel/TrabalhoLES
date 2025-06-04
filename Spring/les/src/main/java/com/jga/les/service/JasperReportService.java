package com.jga.les.service;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;


@Service
public class JasperReportService {
    @Autowired
    private DataSource dataSource;

    public <T> byte[] getRelatorio(String fileName, String title) throws JRException, IOException {
        String jrxmlPath = "src/main/resources/report/"+fileName+".jrxml";
        String jasperOutputPath = "src/main/resources/report/"+fileName+".jasper";

        JasperCompileManager.compileReportToFile(jrxmlPath, jasperOutputPath);
        // JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
        Map<String, Object> parameters = new HashMap<>();
        //parameters.put("title", title);
 
         try (Connection connection = dataSource.getConnection()) { // 2. Obtém conexão JDBC
        // 3. Preenche o relatório usando a conexão JDBC
        JasperPrint jasperPrint = JasperFillManager.fillReport(
            jasperOutputPath,
            parameters,
            connection // Fonte de dados é a conexão JDBC
        );
            byte[] reportContent = JasperExportManager.exportReportToPdf(jasperPrint);

            return reportContent;
        } catch (Exception e) {
            System.out.println("\n\n\n--Erro ao gerar relatório: " + e.getMessage()+"\n\n\n");
            return null;//throw new RuntimeException("Erro ao gerar relatório: " + e.getMessage(), e);
        }

        //Fill report
        // JasperPrint jasperPrint = JasperFillManager.fillReport(jasperOutputPath, parameters, dataSource);
        // byte[] reportContent = JasperExportManager.exportReportToPdf(jasperPrint);

        // return reportContent;
    }
}
