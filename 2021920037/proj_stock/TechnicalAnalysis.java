package proj_stock;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IntSummaryStatistics;

public class TechnicalAnalysis extends JFrame {

    public TechnicalAnalysis(ArrayList<Double> li1, ArrayList<Double> li2, String date1) {
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Stock Total Returns Over Time",
                "Date", "Total Returns",
                createDataset(li1, li2, date1),
                PlotOrientation.VERTICAL,
                true, true, false);

        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        customizeTickLabels(domainAxis,date1);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    public DefaultCategoryDataset createDataset(ArrayList<Double> li1, ArrayList<Double> li2, String date) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        CSVManger csvManger = new CSVManger();

        ArrayList<Double> prices1 = li1;
        ArrayList<String> date1 = csvManger.readCSVDate("SPY");
        ArrayList<Double> prices2 = li2;
        ArrayList<String> date2 = csvManger.readCSVDate(date);

        for (int i = 0; i < prices1.size(); i++) {
            dataset.addValue(prices1.get(i), "ETF1 Total Returns", date1.get(i));
        }

        for (int i = 0; i < prices2.size(); i++) {
            dataset.addValue(prices2.get(i), "ETF2 Total Returns", date2.get(i));
        }

        return dataset;
    }

    public void customizeTickLabels(CategoryAxis domainAxis, String date1) {
        ArrayList<String> dates = new ArrayList<>();
        dates.addAll(new CSVManger().readCSVDate("SPY"));
        dates.addAll(new CSVManger().readCSVDate(date1));

        String currentYear = "";

        for (String date : dates) {
            String year = date.substring(0, 4);

            if (!year.equals(currentYear)) {
                currentYear = year;
                domainAxis.setTickLabelPaint(date, java.awt.Color.BLACK);
            } else {
                domainAxis.setTickLabelPaint(date, new java.awt.Color(0, 0, 0, 0));
            }
        }
    }

    public void printChart(ArrayList<Double> li1, ArrayList<Double> li2, String date1) {
        SwingUtilities.invokeLater(() -> {
            TechnicalAnalysis chart = new TechnicalAnalysis(li1, li2, date1);
            chart.setSize(1000, 600);
            chart.setLocationRelativeTo(null);
            chart.setVisible(true);
            sharpeRatio(li1);
            sharpeRatio(li2);
        });
    }
    public void sharpeRatio(ArrayList<Double> li1){
        System.out.println("사후적 샤프지수 출력");
        CSVManger csvManger = new CSVManger();
        ArrayList<Double> list1 = li1;
        ArrayList<Double> list2 = csvManger.readCSV("SPY");

        if (list1.size() != list2.size()) {
            System.out.println("리스트 크기가 일치하지 않습니다.");
            return;
        }

        double sum = 0;
        double sumSPY = 0;

        // SPY의 평균 수익률 계산
        for (double value : list2) {
            sumSPY += value;
        }
        double riskFreeRate = sumSPY / list2.size();

        ArrayList<Double> excessReturns = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            double excessReturn = list1.get(i) - riskFreeRate;
            excessReturns.add(excessReturn);
            sum += excessReturn;
        }

        double averageExcessReturn = sum / list1.size();

        // 표준 편차 계산
        double sumSquaredDeviations = 0;
        for (double excessReturn : excessReturns) {
            sumSquaredDeviations += Math.pow(excessReturn - averageExcessReturn, 2);
        }
        double standardDeviation = Math.sqrt(sumSquaredDeviations / (list1.size() - 1));

        double sharpeRatio = averageExcessReturn / standardDeviation;

        // 최대 낙폭 계산
        double max = list1.get(0);
        double min = 0;
        double minus = 0;

        for (double j : list1) {
            if (j >= max) {
                max = j;
            } else {
                minus = max - j;
                if (min <= minus) {
                    min = minus;
                }
            }
        }

        System.out.println("샤프지수: " + sharpeRatio);
        System.out.println("고점 대비 최대 낙폭: -" + (min / max * 100) + "%");
        System.out.println("------------------------------------");
    }



    public static void main(String[] args) {
        CSVManger csvManger = new CSVManger();
        ArrayList<Double> li1 = csvManger.readCSV("SPY");
        ArrayList<Double> li2 = csvManger.readCSV("TQQQ");
        String date1 = "SPY";
        String date2 = "TQQQ";
        new TechnicalAnalysis(li1, li2,date2).printChart(li1, li2, date2);
//        new TechnicalAnalysis(li1, li2,date2).sharpeRatio(li1);
    }
}
