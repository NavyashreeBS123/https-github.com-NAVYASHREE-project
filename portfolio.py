"""
Investment Portfolio Tracker
----------------------------
Default mode uses sample_stock_prices.csv (offline).
Optional live mode uses Yahoo Finance through yfinance:
    pip install yfinance
    python portfolio.py --live

The generated portfolio_summary.csv is an append-only transaction log.
Sample prices are illustrative, not live market quotes.
"""

import argparse
import csv
from datetime import datetime
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent
PRICE_FILE = BASE_DIR / "sample_stock_prices.csv"
SUMMARY_FILE = BASE_DIR / "portfolio_summary.csv"


def load_sample_prices():
    """Return {ticker: current_price} from the local sample price CSV."""
    prices = {}
    if not PRICE_FILE.exists():
        raise FileNotFoundError(
            f"Missing {PRICE_FILE.name}. Keep it in the same folder as portfolio.py."
        )

    with PRICE_FILE.open("r", newline="", encoding="utf-8") as file:
        for row in csv.DictReader(file):
            ticker = row["ticker"].strip().upper()
            prices[ticker] = float(row["current_price"])
    return prices


def get_live_price(ticker):
    """Fetch the latest available price using yfinance (requires internet)."""
    try:
        import yfinance as yf
    except ImportError as exc:
        raise RuntimeError(
            "Live mode requires yfinance. Install it with: pip install yfinance"
        ) from exc

    history = yf.Ticker(ticker).history(period="5d", interval="1d")
    if history.empty:
        raise ValueError(f"No recent price found for {ticker}. Check the ticker symbol.")
    return float(history["Close"].dropna().iloc[-1])


def get_positive_float(prompt):
    while True:
        try:
            value = float(input(prompt).strip())
            if value <= 0:
                print("Please enter a number greater than zero.")
                continue
            return value
        except ValueError:
            print("Invalid number. Try again.")


def get_ticker(prompt="Ticker symbol: "):
    while True:
        ticker = input(prompt).strip().upper()
        if ticker and all(ch.isalnum() or ch in ".-^=" for ch in ticker):
            return ticker
        print("Enter a valid ticker symbol, such as AAPL or MSFT.")


def append_summary(rows):
    """Append this run's holding-level results to the CSV log."""
    fieldnames = [
        "timestamp", "ticker", "shares", "buy_price", "current_price",
        "invested_amount", "market_value", "gain_loss", "gain_loss_percent"
    ]
    file_exists = SUMMARY_FILE.exists() and SUMMARY_FILE.stat().st_size > 0

    with SUMMARY_FILE.open("a", newline="", encoding="utf-8") as file:
        writer = csv.DictWriter(file, fieldnames=fieldnames)
        if not file_exists:
            writer.writeheader()
        writer.writerows(rows)


def main():
    parser = argparse.ArgumentParser(description="Track an investment portfolio.")
    parser.add_argument(
        "--live", action="store_true",
        help="Fetch current prices from Yahoo Finance using yfinance."
    )
    args = parser.parse_args()

    sample_prices = {} if args.live else load_sample_prices()
    portfolio = {}

    print("\n" + "=" * 58)
    print("             INVESTMENT PORTFOLIO TRACKER")
    print("=" * 58)
    print("Enter each holding. Type DONE when you have finished.\n")

    while True:
        ticker = input("Ticker symbol (or DONE): ").strip().upper()
        if ticker == "DONE":
            break
        if not ticker or not all(ch.isalnum() or ch in ".-^=" for ch in ticker):
            print("Please enter a valid ticker symbol.")
            continue

        buy_price = get_positive_float("Purchase price per share: ")
        shares = get_positive_float("Number of shares: ")

        try:
            current_price = get_live_price(ticker) if args.live else sample_prices[ticker]
        except KeyError:
            print(
                f"{ticker} is not in {PRICE_FILE.name}. "
                "Add it to that CSV or run with --live."
            )
            continue
        except (ValueError, RuntimeError, ImportError) as error:
            print(f"Could not get a price for {ticker}: {error}")
            continue

        # Store positions by ticker; repeated entries for the same ticker are combined.
        position = portfolio.setdefault(
            ticker, {"shares": 0.0, "invested": 0.0, "current_price": current_price}
        )
        position["shares"] += shares
        position["invested"] += buy_price * shares
        position["current_price"] = current_price
        print(f"Added {ticker}: {shares:g} share(s) at {buy_price:.2f}; "
              f"current price {current_price:.2f}\n")

    if not portfolio:
        print("\nNo holdings entered. Nothing to summarize.")
        return

    timestamp = datetime.now().astimezone().isoformat(timespec="seconds")
    log_rows = []
    total_invested = 0.0
    total_market_value = 0.0

    print("\n" + "=" * 92)
    print(f"{'TICKER':<10}{'SHARES':>10}{'BUY/SHARE':>14}{'CURRENT':>14}"
          f"{'INVESTED':>14}{'MARKET VALUE':>16}{'GAIN/LOSS':>14}")
    print("-" * 92)

    for ticker, position in portfolio.items():
        shares = position["shares"]
        invested = position["invested"]
        current_price = position["current_price"]
        market_value = shares * current_price
        gain_loss = market_value - invested
        gain_loss_percent = (gain_loss / invested) * 100 if invested else 0.0
        average_buy_price = invested / shares

        total_invested += invested
        total_market_value += market_value

        print(f"{ticker:<10}{shares:>10.2f}{average_buy_price:>14.2f}"
              f"{current_price:>14.2f}{invested:>14.2f}{market_value:>16.2f}"
              f"{gain_loss:>14.2f} ({gain_loss_percent:+.2f}%)")

        log_rows.append({
            "timestamp": timestamp,
            "ticker": ticker,
            "shares": f"{shares:.4f}",
            "buy_price": f"{average_buy_price:.4f}",
            "current_price": f"{current_price:.4f}",
            "invested_amount": f"{invested:.2f}",
            "market_value": f"{market_value:.2f}",
            "gain_loss": f"{gain_loss:.2f}",
            "gain_loss_percent": f"{gain_loss_percent:.2f}",
        })

    total_gain_loss = total_market_value - total_invested
    total_gain_loss_percent = (
        total_gain_loss / total_invested * 100 if total_invested else 0.0
    )

    print("-" * 92)
    print(f"Total invested capital : {total_invested:,.2f}")
    print(f"Total market value     : {total_market_value:,.2f}")
    print(f"Total gain / loss      : {total_gain_loss:+,.2f} "
          f"({total_gain_loss_percent:+.2f}%)")
    print("=" * 92)

    append_summary(log_rows)
    print(f"\nTransaction summary saved to: {SUMMARY_FILE.name}")
    if not args.live:
        print("NOTE: Sample prices are illustrative. Use --live for Yahoo Finance prices.")


if __name__ == "__main__":
    main()
