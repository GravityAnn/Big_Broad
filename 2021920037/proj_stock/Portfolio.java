package proj_stock;

import java.util.ArrayList;
import java.util.Scanner;

public class Portfolio {
    Scanner sc = new Scanner(System.in);
    public int choice1;
    public int choice2;

    public void portfolioSystem() {
        ArrayList<String>[] ETFSystem1 = new ArrayList[10];
        ArrayList<String>[] ETFSystem2 = new ArrayList[10];

        for (int i = 0; i < 10; i++) {
            ETFSystem1[i] = new ArrayList<String>();
            ETFSystem2[i] = new ArrayList<String>();
        }
        boolean home = true;

        while (home) {
            boolean back = true;
            System.out.println("ETF를 설계합니다.");
            System.out.println("1,2,3번 중에서 선택해주세요.");
            System.out.println(" 선택1 : - ETF1 - ");
            System.out.println(" 선택2 : - ETF2 - ");
            System.out.println(" 선택3 : - 나가기 - ");
            System.out.printf("입력 : ");
            choice1 = sc.nextInt();
            sc.nextLine();

            ArrayList<String>[] currentETFSystem = null;
            if (choice1 == 1) {
                currentETFSystem = ETFSystem1;
            } else if (choice1 == 2) {
                currentETFSystem = ETFSystem2;
            } else if (choice1 == 3) {
                System.out.println("시스템을 종료합니다..");
                home = false;
                back = false;
                continue;
            }

            while (back) {
                back = true;
                System.out.println("1 ~ 5번 중에서 선택해주세요.");
                System.out.println("1번. 주식 추가");
                System.out.println("2번. 주식 삭제");
                System.out.println("3번. ETF" + choice1 + " 출력");
                System.out.println("4번. 그래프 출력");
                System.out.println("5번. 기술적 분석");
                System.out.println("6번. 뒤로가기");

                System.out.printf("1 ~ 5 중 입력 : ");
                choice2 = sc.nextInt();
                sc.nextLine();
                if(!(1<=choice2 && choice2<=6)){
                    System.out.println("\n1 ~ 6중에서 선택해주세요.\n");
                    continue;
                }
                System.out.println("");
                switch (choice2) {
                    case 1:
                        addStock(currentETFSystem);
                        System.out.println("ETF" + choice1 + "에 주식을 추가합니다.");
                        break;
                    case 2:
                        removeStock(currentETFSystem);
                        System.out.println("ETF" + choice1 + "에 주식을 삭제합니다.");
                        break;
                    case 3:
                        printStock(currentETFSystem);
                        System.out.println("ETF" + choice1 + "을 출력합니다");
                        break;
                    case 4:
                        System.out.println("그래프 출력");
                        CSVManger csvManger = new CSVManger();
                        ArrayList<Double> li1 = csvManger.balancingETF0(ETFSystem1);
                        ArrayList<Double> li2 = csvManger.balancingETF0(ETFSystem2);
                        new TechnicalAnalysis(li1, li2, "SPY").printChart(li1, li2, "SPY");

                        break;
                    case 5:
                        System.out.println("기술적 분석");
                        CSVManger csvManger2 = new CSVManger();
                        System.out.println("ETF"+choice1+"을 VIX와 비교합니다.");
                        ArrayList<Double> li3 = csvManger2.balancingETF0(ETFSystem1);
                        ArrayList<Double> li4 = csvManger2.readCSV("VIX");
                        new TechnicalAnalysis(li3, li4, "VIX").printChart(li3, li4, "VIX");
                        break;
                    case 6:
                        System.out.println("뒤로 가기..");
                        back = false;
                        break;
                    default:
                        System.out.println("출력 중...");
                        break;
                }
            }
        }
    }
    public ArrayList<String>[] addStock(ArrayList<String>[] ETFSystem) {
        System.out.println("주식의 티커 입력");
        String ticker = sc.next();
        boolean tickerExist = false;


        for (ArrayList<String> etf : ETFSystem) {
            if (etf != null && etf.contains(ticker)) {//티커가 있다면,
                System.out.println("총 금액 입력");
                String addValue = sc.next();
                System.out.println("입력 완료");

                String currentValue = etf.get(1);
                double newValue = Double.parseDouble(currentValue) + Double.parseDouble(addValue);
                etf.set(1, String.valueOf(newValue));
                return ETFSystem;
            }
        }
       if (!tickerExist) {
           System.out.println("총 금액 입력");
           for (ArrayList<String> etf_ : ETFSystem) {
               if (etf_.isEmpty()) {
                   etf_.add(ticker);
                   String inValue1 = sc.next();
                   double inValue2 = Double.parseDouble(inValue1);
                   etf_.add(String.valueOf(inValue2));

                   System.out.println("주식이 성공적으로 추가되었습니다..");
                   return ETFSystem;
               }
           }
       }

        return ETFSystem;
    }


    public ArrayList<String>[] removeStock(ArrayList<String>[] ETFSystem) {
        System.out.println("주식의 티커 입력");
        String ticker = sc.next();
        boolean tickerExist = false;

        for (ArrayList<String> etf : ETFSystem) {
            if (etf != null && !etf.isEmpty() && etf.contains(ticker)) { // ETF 리스트가 null이 아니고 비어있지 않으며, 티커가 포함된 경우
                tickerExist = true;
                System.out.println("줄일 금액 입력");
                String addValue = sc.next();

                int index = etf.indexOf(ticker) + 1;
                if (index < etf.size()) { // 인덱스가 리스트 크기 내에 있는지 확인
                    String currentValue = etf.get(index);
                    double newValue = Double.parseDouble(currentValue) - Double.parseDouble(addValue);
                    if (newValue <= 0) {
                        etf.clear();
                        System.out.println("총 금액이 0이므로 삭제되었습니다.");
                        tickerExist = false;
                    } else {
                        etf.set(index, String.valueOf(newValue));
                        System.out.println("금액이 성공적으로 줄어들었습니다");
                    }
                }
                break;
            }
        }
        if (!tickerExist) {//원래는 없음
            System.out.println("해당 주식을 가지고 있지 않습니다.");

        }
        return ETFSystem;
    }
    public void printStock(ArrayList<String>[] ETFSystem){
        System.out.println("< "+ETFSystem+" 출력>");
        int i = 1;
        double sum = 0;
        double ratio = 0;
        for(ArrayList<String> etf1 : ETFSystem){

            if(etf1 != null && !etf1.isEmpty()){
                sum = sum + Double.parseDouble(etf1.get(1));
            }

        }

        for(ArrayList<String> etf2 : ETFSystem){
            if(etf2 != null && !etf2.isEmpty()){
                ratio = Double.parseDouble(etf2.get(1))/sum*100;
                System.out.printf("| "+i+"번 ticker: [%s] | 총 금액: %s$ | 비율: %.2f%% \n",etf2.get(0),etf2.get(1),ratio);
                i++;
            }else{
                System.out.println("----------------------------------------");
                System.out.printf("ETF 포트폴리오 총 금액 : %.2f원\n", sum);
                break;
            }

        }

    }

    }



