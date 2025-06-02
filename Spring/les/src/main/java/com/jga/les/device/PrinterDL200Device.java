package com.jga.les.device;

import com.jga.les.service.ProdutoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

@Component
public class PrinterDL200Device {
    private static final String NOME_IMPRESSORA = "Tally Dascom DL-200Z (Copiar 1)"; // Nome da impressora, se necessário
    @Autowired
    private ProdutoService produtoService;
    private PrintService impressora = null;

    @PostConstruct
    public void init() {
        try {
            String nomeImpressora = NOME_IMPRESSORA;

            // Busca a impressora instalada
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

            for (PrintService service : services) {
                System.out.println("Impressora encontrada: " + service.getName());
                if (service.getName().equalsIgnoreCase(nomeImpressora)) {
                    impressora = service;
                    break;
                }
            }

            if (impressora == null) {
                System.out.println("Impressora não encontrada!");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imprimirLinha(String codigo, int quantidade, String nomeProduto){
        StringBuilder zpl = new StringBuilder("^XA\n");
        zpl.append("^MNY\n"); // Desativa o modo tear-off (ativar impressão contínua)

        zpl.append("^MD30  ; Aumenta a temperatura\n");
        zpl.append("^PR2,2,1  ; Diminui a velocidade para evitar falhas\n");

        // Configuração de posição
        int posicaoX = 40;
        //int posicaoY = -15;
        int posicaoY = 0;
        int larguraEtiqueta = 280; // Distância entre etiquetas
        //int alturaLinha = 0; // Distância entre linhas
        int alturaLinha = 200; // Distância entre linhas

        for (int i = 0; i < quantidade; i++) {
            int coluna = i % 3; // 0, 1 ou 2 (posição da etiqueta na linha)
            int linha = i / 3;  // Muda para nova linha quando necessário

            int x = posicaoX + (coluna * larguraEtiqueta);
            int y = posicaoY + (linha * alturaLinha); // Distância ajustada para evitar sobreposição
            System.out.print(x);
            System.out.println("-"+y);
            // Nome do produto
            zpl.append("^FO").append(x).append(",").append(y)
                    .append("^A0N,40,40^FD").append(nomeProduto).append("^FS\n");

            if(codigo.length()>6){
                // Código de barras com largura reduzida
                zpl.append("^FO").append(x).append(",").append(y + 70)
                    .append("^BY1.2^BCN,60,Y,N,N^FD").append(codigo).append("^FS\n");
            }else{
                // Código de barras com largura reduzida
                zpl.append("^FO").append(x).append(",").append(y + 70)
                    .append("^BY1.5^BCN,60,Y,N,N^FD").append(codigo).append("^FS\n");
            }
        }

        zpl.append("^XZ");

        // Criando o documento para impressão
        DocPrintJob job = impressora.createPrintJob();
        Doc doc = new SimpleDoc(zpl.toString().getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
        try {
            job.print(doc, null);
        } catch (PrintException e) {
            e.printStackTrace();
        }
        System.out.println("Impressão enviada com sucesso!");
    }

    public void imprimirCodigoBarras(String codigo, Integer quantidade) {
        // Ajustando os códigos para caberem nas 3 etiquetas por linha
        String nomeProduto = produtoService.findByCodigo(codigo).getNome(); //DESCOMENTAR QUANDO FOR APLICAR
        int row = quantidade / 3;
        int col_restante = quantidade%3;

        for(int i=0; i<row; i++){
            imprimirLinha(codigo, 3, nomeProduto);
        }
        imprimirLinha(codigo, col_restante, nomeProduto);
    }
}
