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
using Newtonsoft.Json;

namespace StockAnalysis.Models
{
    public class Company
    {

        public string Symbol { get; set; }


        public string CompanyName { get; set; }

        public string Exchange { get; set; }

        public string Industry { get; set; }

        public string Website { get; set; }

        public string Description { get; set; }

        public string CEO { get; set; }

        public string IssueType { get; set; }

        public string Sector { get; set; }

        public List<string> Tags { get; set; }






    }
}