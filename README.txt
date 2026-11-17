# Investment Portfolio Tracker

## Files
- `portfolio.py` — interactive portfolio tracker.
- `sample_stock_prices.csv` — offline demonstration prices (illustrative only).
- `portfolio_summary.csv` — generated append-only transaction summary; starts with headers.

## Run
Open a terminal in this folder and run:

```bash
python portfolio.py
```

Enter ticker symbols, purchase price per share, and number of shares. Enter `DONE`
when finished. The script prints holding-level and total portfolio performance,
then appends the run's summary to `portfolio_summary.csv`.

Supported sample tickers: AAPL, MSFT, GOOGL, AMZN, NVDA, TSLA.

## Optional live prices
Install the optional package and run:

```bash
pip install yfinance
python portfolio.py --live
```

Live prices require an internet connection. Yahoo Finance data may be delayed or
unavailable for some symbols. The sample CSV supplied here contains demonstration
prices, not real-time market data.

## Formulae
- Invested amount = shares × purchase price per share
- Market value = shares × current price
- Gain/loss = market value − invested amount
- Gain/loss % = (gain/loss ÷ invested amount) × 100
