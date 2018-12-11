using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using Newtonsoft.Json;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Newtonsoft.Json.Linq;
using StockAnalysis.Models;

namespace StockAnalysis
{
    public class RestService
    {
        HttpClient client;
        public List<String> Items { get; private set; }
        public Company Company { get; private set; }
        public List<Quote> Quotes { get; private set; }
        String RestUrl = "https://api.iextrading.com/1.0/stock/aapl/chart";

        public RestService()
        {
            client = new HttpClient();
            client.MaxResponseContentBufferSize = 256000;
        }

        public async Task<List<String>> RefreshDataAsync()
        {
            Items = new List<String>();

            var uri = new Uri(string.Format(RestUrl, string.Empty));
            try
            {
                var response = await client.GetAsync(uri);
                if (response.IsSuccessStatusCode)
                {
                    var content = await response.Content.ReadAsStringAsync();

                    //content = "{\"quotes\":" + content + "}";

                    Quotes = JsonConvert.DeserializeObject<List<Quote>>(content);
                    Items.Add(content);
                    System.Diagnostics.Debug.WriteLine(Quotes);
                }
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine(ex);
            }



            return Items;
        }


    }
}