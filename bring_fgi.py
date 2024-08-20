from datetime import datetime, date, timedelta
import math
import pandas as pd
import fear_and_greed
import yfinance as yf

def get_fear_and_greed():
    fg = fear_and_greed.get()
    print("FGI: ",fg)
    fg_score = float(fg[0])
    fg_score = int(round(fg_score,0))
    fg_status = fg[1]
    fg_date = fg[2].date()
    print("fg_date : ",fg_date)

    return (fg_score, fg_status)

def buy_stock(ticker):
    print("Buy some tqqq")
    #slackout("Buy some tqqq")

def sell_stock(ticker):
    print("Sell some tqqq")
    #slackout("Sell some tqqq")

def main():
    sell_score = 75
    buy_score = 25

    try:
        fg_score, fg_status = get_fear_and_greed()

        if fg_score > sell_score:
            sell_stock()
        if fg_score > sell_score:
            buy_stock()

        print(fg_score, fg_status)

    except Exception as ex:
        print("예외처리 :" + str(ex))

if __name__ == "__main__":
    main()
