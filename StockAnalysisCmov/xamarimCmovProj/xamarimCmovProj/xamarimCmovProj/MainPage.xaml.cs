using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;



using SkiaSharp;
using SkiaSharp.Views.Forms;
using Xamarin.Forms;
using xamarimCmovProj.Models;


namespace xamarimCmovProj
{
    public partial class MainPage : ContentPage
    {
        List<Company> MainCompanies { get; set; }
        List<Quote> MainQuotes { get; set; }
        List<Quote> SecondaryQuotes { get; set; }
        public RestService rest = new RestService();
        int selectedIndex1 = -1;
        int selectedIndex2 = -1;
        String dateOfSeing = "";
        Boolean is7days = true;

        Dictionary<string, string> companies = new Dictionary<string, string>
        {
            { "Apple", "AAPL"}, { "IBM", "IBM"},
            { "Hewlett Packard", "HPE"}, { "Microsoft", "MSFT"},
            { "Oracle", "ORCL"}, { "Spotify", "SPOT"},
            { "Facebook", "FB"}, { "Twitter", "TWTR"},
            { "Intel", "INTC"}, { "AMD", "AMD"},
            { "--Empty--", "empty"}
        };

        SKPaint box = new SKPaint
        {
            Color = Color.Black.ToSKColor(),
            StrokeWidth = 2
        };

        public MainPage()
        {
            InitializeComponent();

            DateTime today = DateTime.Today;
            DateTime sevenDaysEarlier = today.AddDays(-9);
            dateOfSeing = sevenDaysEarlier.ToString("yyyyMMdd");

            MainQuotes = rest.QuoteRefreshDataAsync("aapl", dateOfSeing).Result;
            SecondaryQuotes = rest.QuoteRefreshDataAsync("ibm", dateOfSeing).Result;
            Title = "Graph";
        }


        void OnPickerSelectedIndexChanged(object sender, EventArgs e)
        {
            var picker = (Picker)sender;
            selectedIndex1 = picker.SelectedIndex;
            if (selectedIndex1 == companies.Count - 1)
            {
                MainQuotes.Clear();
                canvasView.InvalidateSurface();
            }
            else if (selectedIndex1 != -1)
            {
                MainQuotes = rest.QuoteRefreshDataAsync(companies[picker.Items[selectedIndex1]], dateOfSeing).Result;
                canvasView.InvalidateSurface();
            }

        }

        void OnPickerSelectedIndexChanged2(object sender, EventArgs e)
        {
            var picker = (Picker)sender;
            selectedIndex2 = picker.SelectedIndex;

            if (selectedIndex2 == companies.Count - 1)
            {
                SecondaryQuotes.Clear();
                canvasView.InvalidateSurface();
            }
            else if (selectedIndex2 != -1)
            {
                SecondaryQuotes = rest.QuoteRefreshDataAsync(companies[(picker.Items[selectedIndex2])], dateOfSeing).Result;
                canvasView.InvalidateSurface();
            }
        }

        void OnCanvasViewPaintSurface(object sender, SKPaintSurfaceEventArgs args)
        {
            SKImageInfo info = args.Info;
            SKSurface surface = args.Surface;
            SKCanvas canvas = surface.Canvas;

            canvas.Clear();

            float max = 0;
            float min = 0;
            float mmax = 0;
            float mmin = 0;
            float smax = 0;
            float smin = 0;
            if (SecondaryQuotes.Count != 0)
            {
                smax = (float)SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[0].Close;
                smin = (float)SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[SecondaryQuotes.Count - 1].Close;
            }
            if (MainQuotes.Count != 0)
            {
                mmax = (float)MainQuotes.OrderByDescending(l => l.Close).ToList()[0].Close;
                mmin = (float)MainQuotes.OrderByDescending(l => l.Close).ToList()[MainQuotes.Count - 1].Close;

                if (SecondaryQuotes.Count != 0)
                {
                    if (mmax > smax)
                        max = mmax;
                    else
                        max = smax;

                    if (mmin < smin)
                        min = mmin;
                    else
                        min = smin;
                }
                else
                {
                    max = (float)MainQuotes.OrderByDescending(l => l.Close).ToList()[0].Close;
                    min = (float)MainQuotes.OrderByDescending(l => l.Close).ToList()[MainQuotes.Count - 1].Close;
                }
            }
            else if (SecondaryQuotes.Count != 0)
            {
                max = (float)SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[0].Close;
                min = (float)SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[SecondaryQuotes.Count - 1].Close;
            }

            if (MainQuotes.Count > SecondaryQuotes.Count)
                canvas.Scale(1, info.Height / (Math.Max(max, 100) * 1.5f), info.Width / MainQuotes.Count, min);
            else if (MainQuotes.Count != 0 || SecondaryQuotes.Count != 0)
                canvas.Scale(1, info.Height / (Math.Max(max, 100) * 1.5f), info.Width / SecondaryQuotes.Count, min);

            canvas.DrawLine(0, canvas.LocalClipBounds.Bottom - 35, info.Width, canvas.LocalClipBounds.Bottom - 35, box);
            canvas.DrawLine(0, 0, 0, canvas.LocalClipBounds.Bottom - 35, box);
            createQuotes(MainQuotes, true, info, canvas, mmax, smax, Color.Red.ToSKColor());
            createQuotes(SecondaryQuotes, false, info, canvas, mmax, smax, Color.Blue.ToSKColor());
        }

        void createQuotes(List<Quote> quotes, Boolean isFirst, SKImageInfo info, SKCanvas canvas, float mmax, float smax, SKColor sKColor)
        {
            if (quotes.Count == 0)
                return;
            SKPaint paint = new SKPaint
            {
                Style = SKPaintStyle.Fill,
                Color = sKColor,
                StrokeWidth = 0.7f
            };
            float[] fl = new float[] { 4, 4 };
            float[] fl2 = new float[] { 6, 6 };

            SKPaint textPaint = new SKPaint
            {
                Style = SKPaintStyle.Fill,
                Color = SKColors.Black,
                StrokeWidth = 2f
            };

            SKPaint paintDash = new SKPaint
            {
                Style = SKPaintStyle.Stroke,
                Color = Color.Gray.ToSKColor(),
                StrokeWidth = 2f,
                PathEffect = SKPathEffect.CreateDash(fl, 20)
            };
            SKPaint paintDot = new SKPaint
            {
                Style = SKPaintStyle.Stroke,
                Color = Color.Gray.ToSKColor(),
                StrokeWidth = 0.7f,
                PathEffect = SKPathEffect.CreateDash(fl2, 20)
            };

            SKPaint npaintDash = new SKPaint
            {
                Style = SKPaintStyle.Stroke,
                Color = Color.Gray.ToSKColor(),
                StrokeWidth = 0.7f,
                PathEffect = SKPathEffect.CreateDash(fl, 20)
            };


            SKPath path = new SKPath();
            SKPath npath = new SKPath();

            double dmmax = quotes.OrderByDescending(l => l.Close).ToList()[0].Close;
            double dmmin = quotes.OrderByDescending(l => l.Close).ToList()[quotes.Count - 1].Close;

            path.MoveTo(0, canvas.LocalClipBounds.Bottom - 35);
            path.LineTo(0, canvas.LocalClipBounds.Bottom - (float)quotes[0].Close - 35);


            int last = 0;
            for (var i = 0; i < quotes.Count - 1; i++)
            {
                path.LineTo(info.Width / quotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - (float)quotes[i + 1].Close - 35);
                canvas.DrawLine(info.Width / quotes.Count * i, canvas.LocalClipBounds.Bottom - (float)quotes[i].Close - 35, info.Width / quotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - (float)quotes[i + 1].Close - 35, paint);
                last = i + 1;
                double current = quotes[i + 1].Close;

                if (current.Equals(dmmax) || current.Equals(dmmin))
                {
                    canvas.DrawText(Convert.ToString(quotes[i + 1].Close), info.Width / quotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - (float)quotes[i + 1].Close - 35, textPaint);
                }

                if (i % 3 == 0 || is7days)
                {
                    if (isFirst && mmax >= smax)
                    {
                        npath.MoveTo(info.Width / quotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - (float)quotes[i + 1].Close - 35);
                        npath.LineTo(info.Width / quotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - 35);
                    }
                    else if (mmax <= smax)
                    {
                        npath.MoveTo(info.Width / quotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - (float)quotes[i + 1].Close - 35);
                        npath.LineTo(info.Width / quotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - 35);
                    }
                    canvas.Save();
                    canvas.Translate((info.Width / quotes.Count * (i + 1)) - 5, canvas.LocalClipBounds.Bottom - 25);
                    canvas.RotateDegrees(22);
                    canvas.DrawText(quotes[i + 1].Timestamp.ToString("yyyy-MM-dd"), 0, 0, textPaint);
                    canvas.Restore();
                }
            }
            double first = quotes[0].Close;
            if (first.Equals(dmmax) || first.Equals(dmmin))
            {
                canvas.DrawText(Convert.ToString(quotes[0].Close), info.Width / quotes.Count * (0), canvas.LocalClipBounds.Bottom - (float)quotes[0].Close - 35, textPaint);
            }
            path.LineTo(info.Width / quotes.Count * last, canvas.LocalClipBounds.Bottom - 35);
            path.Close();
            paint.Color = sKColor.WithAlpha((byte)(0xFF * 0.2));
            canvas.DrawPath(path, paint);
            canvas.DrawPath(npath, paintDash);
            canvas.DrawLine(0, canvas.LocalClipBounds.Bottom - (float)quotes.OrderByDescending(l => l.Close).ToList()[0].Close - 35, canvas.LocalClipBounds.Right, canvas.LocalClipBounds.Bottom - (float)quotes.OrderByDescending(l => l.Close).ToList()[0].Close - 35, paintDot);
            canvas.DrawLine(0, canvas.LocalClipBounds.Bottom - (float)quotes.OrderByDescending(l => l.Close).ToList()[quotes.Count - 1].Close - 35, canvas.LocalClipBounds.Right, canvas.LocalClipBounds.Bottom - (float)quotes.OrderByDescending(l => l.Close).ToList()[quotes.Count - 1].Close - 35, paintDot);
        }

        private void Button_Company1_Clicked(object sender, EventArgs e)
        {
            string compName = companies[(string)Company1.SelectedItem];
            Task<Company> a = rest.CompanyRefreshDataAsync(compName);
            a.Wait();
            Company c = a.Result;
            Navigation.PushAsync(new DetailsPage(c));
        }

        private void Button_Company2_Clicked(object sender, EventArgs e)
        {
            string compName = companies[(string)Company2.SelectedItem];
            Task<Company> a = rest.CompanyRefreshDataAsync(compName);
            a.Wait();
            Company c = a.Result;
            Navigation.PushAsync(new DetailsPage(c));
        }

        private void Button_7_Clicked(object sender, EventArgs e)
        {
            if (is7days)
                return;
            DateTime today = DateTime.Today;
            DateTime sevenDaysEarlier = today.AddDays(-9);
            dateOfSeing = sevenDaysEarlier.ToString("yyyyMMdd");
            is7days = true;

            string compName1 = companies[(string)Company1.SelectedItem];
            string compName2 = companies[(string)Company2.SelectedItem];

            if (compName1.CompareTo("empty") != 0)
                MainQuotes = rest.QuoteRefreshDataAsync(compName1, dateOfSeing).Result;
            else
                MainQuotes.Clear();

            if (compName2.CompareTo("empty") != 0)
                SecondaryQuotes = rest.QuoteRefreshDataAsync(compName2, dateOfSeing).Result;
            else
                SecondaryQuotes.Clear();
            canvasView.InvalidateSurface();
        }

        private void Button_30_Clicked(object sender, EventArgs e)
        {
            if (!is7days)
                return;
            DateTime today = DateTime.Today;
            DateTime daysEarlier = today.AddDays(-39);
            dateOfSeing = daysEarlier.ToString("yyyyMMdd");
            is7days = false;

            string compName1 = companies[(string)Company1.SelectedItem];
            string compName2 = companies[(string)Company2.SelectedItem];

            if (compName1.CompareTo("empty") != 0)
                MainQuotes = rest.QuoteRefreshDataAsync(compName1, dateOfSeing).Result;
            else
                MainQuotes.Clear();

            if (compName2.CompareTo("empty") != 0)
                SecondaryQuotes = rest.QuoteRefreshDataAsync(compName2, dateOfSeing).Result;
            else
                SecondaryQuotes.Clear();
            canvasView.InvalidateSurface();
        }
    }
}
