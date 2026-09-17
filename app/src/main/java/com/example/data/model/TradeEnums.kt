package com.example.data.model

enum class Market(val displayName: String) {
    FOREX("Forex"),
    CRYPTO("Crypto"),
    STOCKS("Stocks"),
    INDICES("Indices"),
    COMMODITIES("Commodities")
}

enum class Direction(val displayName: String) {
    BUY("BUY / LONG"),
    SELL("SELL / SHORT")
}

enum class TradingSession(val displayName: String) {
    LONDON("London"),
    NEW_YORK("New York"),
    ASIAN("Asian"),
    OVERLAP("London / NY Overlap")
}

enum class Timeframe(val displayName: String) {
    M1("1m"),
    M5("5m"),
    M15("15m"),
    H1("1h"),
    H4("4h"),
    D1("1D")
}

enum class Strategy(val displayName: String) {
    BREAKOUT("Breakout & Retest"),
    TREND_FOLLOWING("Trend Following"),
    SUPPORT_RESISTANCE("Key Support / Resistance"),
    ICT_SMC("ICT / Smart Money Concepts"),
    MEAN_REVERSION("Mean Reversion / VWAP"),
    PRICE_ACTION("Pure Price Action")
}

enum class ScoreCategory(val title: String, val level: String) {
    HIGH_QUALITY("HIGH QUALITY SETUP", "Strong setup based on your checklist"),
    MODERATE("MODERATE SETUP", "Setup requires additional confirmation"),
    WEAK("WEAK SETUP", "Multiple conditions are not confirmed"),
    VERY_WEAK("VERY WEAK SETUP", "High risk - critical criteria missing"),
    DANGER("DANGER / AVOID SETUP", "DANGER — Trade conditions not confirmed")
}

enum class TradeOutcome(val displayName: String) {
    PENDING("Active / Pending"),
    WIN("Win"),
    LOSS("Loss"),
    BREAKEVEN("Break-even")
}

enum class TraderEmotion(val displayName: String) {
    CALM("Calm & Patient"),
    CONFIDENT("Disciplined & Confident"),
    ANXIOUS("Anxious / Hesitant"),
    FOMO("Fear Of Missing Out (FOMO)"),
    GREEDY("Overleveraged / Greedy"),
    REVENGE("Revenge Trading")
}
