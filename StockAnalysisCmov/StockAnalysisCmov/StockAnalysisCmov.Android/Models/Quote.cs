using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace StockAnalysisCmov.Droid.Models
{
    class Quote
    {
        public string Date { get; set; }

        public double Open { get; set; }

        public double High { get; set; }

        public double Low { get; set; }

        public double Close { get; set; }

        public int Volume { get; set; }

        public double UnadjustedClose { get; set; }

        public int UnadjustedVolume { get; set; }

        public double Change { get; set; }

        public double ChangePercent { get; set; }

        public double Vwap { get; set; }

        public string Label { get; set; }

        public double ChangeOverTime { get; set; }
    }
}