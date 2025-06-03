package com.jga.les.pdfGenerator;


import java.awt.image.BufferedImage;
import java.util.List;

public class PlotUtils {

    public static BufferedImage generateConsumoDiarioChart(List<Double> valores, String titulo) {
        // Converter lista de valores para arrays de X (dias) e Y (valores)
        double[] dias = new double[valores.size()];
        double[] totais = new double[valores.size()];

        for (int i = 0; i < valores.size(); i++) {
            dias[i] = i + 1; // Dia 1, Dia 2, etc.
            totais[i] = valores.get(i);
        }

        // Configurar opções do gráfico
        Plot.PlotOptions opts = Plot.plotOpts()
                .title(titulo)
                .width(800)
                .height(400)
                .bgColor(java.awt.Color.WHITE)
                .fgColor(java.awt.Color.BLACK)
                .grids(10, 10);

        // Criar e retornar o gráfico como BufferedImage
        return Plot.plot(opts)
                .series("Consumo Diário",
                        Plot.data().xy(dias, totais),
                        Plot.seriesOpts()
                                .color(java.awt.Color.BLUE)
                                .marker(Plot.Marker.CIRCLE))
                .save();
    }
}