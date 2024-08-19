import yfinance as yf
from datetime import datetime, date, timedelta
import pandas as pd


start = str(date.today() - timedelta(days=1825))
end = str(date.today())
date = yf.download(['SPY'],start=start, end=end, progress=False)
#이곳까지가 datetime, date, timedelta로 날짜 자동화 하기

USD_rate = yf.download(['USDKRW=X'],start=start, end=end, progress=False)

today_rate = USD_rate['Close'].iloc[-1]
today_rate = round(today_rate,2) #현재 환율 구하기
print(today_rate) #현재 환율 프린트

mean_rate = USD_rate['Close'].mean()
mean_rate = round(mean_rate,2) #기간 평균 환율 구하기
print(mean_rate)

if today_rate < mean_rate:
    #slackout('현재 환율이 '+str(today_rate) + '원으로, 환율 5년 평균' +str(mean_rate)+'보다 낮습니다.')
    print('현재 환율이 '+str(today_rate) + '원으로, 환율 5년 평균 ' +str(mean_rate)+'보다 낮습니다.')
elif (mean_rate * 1.1) > today_rate:
    #slackout('현재 환율이 '+str(today_rate) + '원으로, 환율 5년 평균' +str(mean_rate)+'보다 10% 이상 낮습니다.')
    print('현재 환율이 '+str(today_rate) + '원으로, 환율 5년 평균 ' +str(mean_rate)+'보다 10% 이상 높습니다.')
