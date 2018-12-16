using System;
using System.Collections.Generic;
using System.Text;

namespace xamarimCmovProj.Models
{
    public class Status
    {
        public int Code { get; set; }
        public string Message { get; set; }
    }

    public class Quote
    {
        public string Symbol { get; set; }
        public DateTime Timestamp { get; set; }
        public string TradingDay { get; set; }
        public double Open { get; set; }
        public double High { get; set; }
        public double Low { get; set; }
        public double Close { get; set; }
        public int Volume { get; set; }
        public object OpenInterest { get; set; }
    }

    public class AllQuotes
    {
        public Status Status { get; set; }
        public List<Quote> Results { get; set; }
    }
}