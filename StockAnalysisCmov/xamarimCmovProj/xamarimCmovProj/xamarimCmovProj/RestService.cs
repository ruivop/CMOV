using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using Newtonsoft.Json;

using xamarimCmovProj.Models;



namespace xamarimCmovProj
{
    public class RestService
    {
        HttpClient client;
        public List<String> Items { get; private set; }
        public AllQuotes Quotes { get; private set; }
        Company companyRet = null;

        public RestService()
        {
            client = new HttpClient();
            client.MaxResponseContentBufferSize = 256000;
        }

        public async Task<List<Quote>> QuoteRefreshDataAsync(string company, String date)
        {
            Items = new List<String>();
            Uri uri;

            uri = new Uri(string.Format("https://marketdata.websol.barchart.com/getHistory.json?apikey=b9ad6a29dd4888e6deba0eca305384e8&symbol=" + company + "&type=daily&startDate=" + date, string.Empty));


            try
            {
                var response = await client.GetAsync(uri).ConfigureAwait(false);
                response.EnsureSuccessStatusCode();
                if (response.IsSuccessStatusCode)
                {
                    var content = await response.Content.ReadAsStringAsync().ConfigureAwait(false);

                    //content = "{\"quotes\":" + content + "}";

                    Quotes = JsonConvert.DeserializeObject<AllQuotes>(content);
                    Items.Add(content);
                    System.Diagnostics.Debug.WriteLine(content);
                }
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine(ex);
            }



            return Quotes.Results;
        }


        public async Task<Company> CompanyRefreshDataAsync(string company)
        {
            var uri = new Uri(string.Format("https://api.iextrading.com/1.0/stock/" + company + "/company", string.Empty));
            try
            {
                var response = await client.GetAsync(uri).ConfigureAwait(false);
                response.EnsureSuccessStatusCode();
                if (response.IsSuccessStatusCode)
                {
                    var content = await response.Content.ReadAsStringAsync();

                    //content = "{\"quotes\":" + content + "}";

                    companyRet = JsonConvert.DeserializeObject<Company>(content);
                    System.Diagnostics.Debug.WriteLine(content);
                }
            }
            catch (Exception ex)
            {
                System.Diagnostics.Debug.WriteLine(ex);
            }

            return companyRet;
        }

    }
}