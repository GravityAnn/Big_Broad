package proj_stock;

import java.io.*;
import java.util.*;

public class CSVManger {


    public ArrayList<Double> readCSV(String ticker){
        List<List<String>> csvList = new ArrayList<>();
        //csvList 생성
        String address = "C:\\Users\\jhann01\\Desktop\\stock_CSV\\";
        String extension = "_filled.csv";

        File csv = new File(address.concat(ticker.concat(extension)));
        int num = 0;

        try{
            String line = "";

            BufferedReader bufferedReader = new BufferedReader(new FileReader(csv));
            //전체 리스트
            while((line = bufferedReader.readLine()) != null){
                String[] lineArr = line.split(",");

                List<String> aLine = Arrays.asList(lineArr);
                csvList.add(aLine);
                num++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //csvList 생성 완료
        ArrayList<Double> priceList = new ArrayList<Double>();
        double start = Double.parseDouble(csvList.get(1).get(5).toString());

        for (int i = 1; i < num; i++) {
            priceList.add(Double.parseDouble(csvList.get(i).get(5).toString())/start*100-100);
        }
        return priceList;
    }

    public ArrayList<String> readCSVDate(String ticker){
        List<List<String>> csvList = new ArrayList<>();
        //csvList 생성
        String address = "C:\\Users\\jhann01\\Desktop\\stock_CSV\\";
        String extension = "_filled.csv";

        File csv = new File(address.concat(ticker.concat(extension)));
        int num = 0;

        try{
            String line = "";

            BufferedReader bufferedReader = new BufferedReader(new FileReader(csv));
            //전체 리스트
            while((line = bufferedReader.readLine()) != null){
                String[] lineArr = line.split(",");

                List<String> aLine = Arrays.asList(lineArr);
                csvList.add(aLine);
                num++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //csvList 생성 완료
        ArrayList<String> dateList = new ArrayList<String>();
        for (int i = 1; i < num; i++) {
            dateList.add((csvList.get(i).get(0).toString()));
        }
        return dateList;
    }

    public ArrayList<Double> balancingETF0(ArrayList<String>[] ETF0){
        CSVManger csvManger = new CSVManger();

        ArrayList<Double> balancingETF0 = new ArrayList<>(
                Collections.nCopies(csvManger.readCSV("SPY").size(),0.0)
        );

        double sum = 0;
        double ratio = 0;

        for(ArrayList<String> etf1 : ETF0){
            if(etf1 != null && !etf1.isEmpty()){
                sum = sum + Double.parseDouble(etf1.get(1));
            }
        }//sum 완성

        for(ArrayList<String> etf2 : ETF0){
            if(etf2 != null && !etf2.isEmpty()){
                ArrayList<Double> csvRatio = csvManger.readCSV(etf2.get(0));
                System.out.println(csvRatio);
                ratio = Double.parseDouble(etf2.get(1))/sum;

                for (int i = 0; i < csvRatio.size(); i++) {
                    balancingETF0.set(i,csvRatio.get(i)*ratio+balancingETF0.get(i));
                }

            }else{
                break;
            }

        }
        return balancingETF0;
    }


}
