using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;



using SkiaSharp;
using SkiaSharp.Views.Forms;
using Xamarin.Forms;
using StockAnalysisCmov.Models;


namespace StockAnalysisCmov
{
    public partial class MainPage : ContentPage
    {
        List<Company> MainCompanies { get; set; }
        List<Quote> MainQuotes { get; set; }
        List<Quote> SecondaryQuotes { get; set; }
        RestService rest = new RestService();

        Dictionary<string, string> companies = new Dictionary<string, string>
        {
            { "Apple", "AAPL"}, { "IBM", "IBM"},
            { "Hewlett Packard", "HPE"}, { "Microsoft", "MSFT"},
            { "Oracle", "ORCL"}, { "Spotify", "SPOT"},
            { "Facebook", "FB"}, { "Twitter", "TWTR"},
            { "Intel", "INTC"}, { "AMD", "AMD"}

        };

        public MainPage()
        {
            InitializeComponent();
            
            MainQuotes = rest.QuoteRefreshDataAsync("aapl").Result;
            SecondaryQuotes = rest.QuoteRefreshDataAsync("ibm").Result;
            System.Diagnostics.Debug.WriteLine(MainQuotes);
            

            Title = "Graph";

            /*Picker picker = new Picker
            {
                Title = "Company",
                VerticalOptions = LayoutOptions.CenterAndExpand
            };

            foreach (string company in companies.Keys)
            {
                picker.Items.Add(company);
            }*/

            //SKCanvasView canvasView = new SKCanvasView();
            //canvasView.PaintSurface += OnCanvasViewPaintSurface;
            //Content = canvasView;


            //Companies.Add(rest.CompanyRefreshDataAsync().Result);


        }


        void OnPickerSelectedIndexChanged(object sender, EventArgs e)
        {
            var picker = (Picker)sender;
            int selectedIndex = picker.SelectedIndex;

            if (selectedIndex != -1)
            {
                MainQuotes = rest.QuoteRefreshDataAsync(companies[(picker.Items[selectedIndex])]).Result;
                canvasView.InvalidateSurface();
            }
        }

        void OnPickerSelectedIndexChanged2(object sender, EventArgs e)
        {
            var picker = (Picker)sender;
            int selectedIndex = picker.SelectedIndex;

            if (selectedIndex != -1)
            {
                SecondaryQuotes = rest.QuoteRefreshDataAsync(companies[(picker.Items[selectedIndex])]).Result;
                canvasView.InvalidateSurface();
            }
        }

        void OnCanvasViewPaintSurface(object sender, SKPaintSurfaceEventArgs args)
        {
            SKImageInfo info = args.Info;
            SKSurface surface = args.Surface;
            SKCanvas canvas = surface.Canvas;

            canvas.Clear();

            SKPaint box = new SKPaint
            {
               
                Color = Color.Black.ToSKColor(),
                StrokeWidth = 2
            };

            SKPaint paint = new SKPaint
            {
                Style = SKPaintStyle.Fill,
                Color = Color.Red.ToSKColor(),
                StrokeWidth = 0.7f
            };
            float[] fl = new float[] {4,4};
            float[] fl2 = new float[] {6,6};
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
            SKPaint textPaint = new SKPaint
            {
                Style = SKPaintStyle.Fill,
                Color = SKColors.Black,
                StrokeWidth = 2f
            };


            //canvas.DrawCircle(info.Width / 2, info.Height / 2, 100, paint);
            float max = 0;
            float min = 0;
            float mmax = (float)MainQuotes.OrderByDescending(l => l.Close).ToList()[0].Close;
            float mmin = (float)MainQuotes.OrderByDescending(l => l.Close).ToList()[MainQuotes.Count - 1].Close;
            float smax = (float)SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[0].Close;
            float smin = (float)SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[SecondaryQuotes.Count - 1].Close;
            if (MainQuotes.Count != 0)
            {
                if(SecondaryQuotes.Count != 0)
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
            else if(SecondaryQuotes.Count != 0)
            {
                max = (float)SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[0].Close;
                min = (float)SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[SecondaryQuotes.Count - 1].Close;
            }

            if (MainQuotes.Count > SecondaryQuotes.Count)
            {
                canvas.Scale(1, 3f, info.Width / MainQuotes.Count, min);
            }
            else
            {
                canvas.Scale(1, 3f, info.Width / SecondaryQuotes.Count, min);
            }
            
            
            if (MainQuotes.Count != 0)
            {

                SKPath path = new SKPath();
                SKPath npath = new SKPath();

                double dmmax = MainQuotes.OrderByDescending(l => l.Close).ToList()[0].Close;
                double dmmin = MainQuotes.OrderByDescending(l => l.Close).ToList()[MainQuotes.Count - 1].Close;



                canvas.DrawLine(0, canvas.LocalClipBounds.Bottom - 35, info.Width, canvas.LocalClipBounds.Bottom - 35, box);
                canvas.DrawLine(0, 0, 0, canvas.LocalClipBounds.Bottom - 35, box);
                
                path.MoveTo(0, canvas.LocalClipBounds.Bottom - 35);
                path.LineTo(0, canvas.LocalClipBounds.Bottom - (float)MainQuotes[0].Close - 35);
                

                int last = 0;
                for (var i = 0; i < MainQuotes.Count - 1; i++)
                {
                    path.LineTo(info.Width / MainQuotes.Count * (i+1), canvas.LocalClipBounds.Bottom -(float)MainQuotes[i+1].Close - 35);
                    canvas.DrawLine(info.Width / MainQuotes.Count * i, canvas.LocalClipBounds.Bottom - (float)MainQuotes[i].Close - 35, info.Width / MainQuotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - (float)MainQuotes[i + 1].Close - 35, paint);
                    last = i+1;
                    double current = MainQuotes[i+1].Close;

                    if (current.Equals(dmmax) || current.Equals(dmmin))
                    {
                        canvas.DrawText(Convert.ToString(MainQuotes[i+1].Close), info.Width / MainQuotes.Count * (i+1), canvas.LocalClipBounds.Bottom - (float)MainQuotes[i + 1].Close - 35, textPaint);
                    }

                        if (i % 3 == 0)
                    {
                        if (mmax >= smax)
                        {
                            npath.MoveTo(info.Width / MainQuotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - (float)MainQuotes[i + 1].Close - 35);
                            npath.LineTo(info.Width / MainQuotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - 35);
                        }
                        canvas.Save();
                        canvas.Translate((info.Width / MainQuotes.Count * (i+1))- 5, canvas.LocalClipBounds.Bottom - 25);
                        canvas.RotateDegrees(22);
                        canvas.DrawText(MainQuotes[i+1].Date, 0, 0, textPaint);
                        canvas.Restore();

                    }
                }
                double first = MainQuotes[0].Close;
                if (first.Equals(dmmax) || first.Equals(dmmin))
                {
                    canvas.DrawText(Convert.ToString(MainQuotes[0].Close), info.Width / MainQuotes.Count * (0), canvas.LocalClipBounds.Bottom - (float)MainQuotes[0].Close - 35, textPaint);
                }
                path.LineTo(info.Width / MainQuotes.Count * last, canvas.LocalClipBounds.Bottom - 35);
                path.Close();
                paint.Color = SKColors.Red.WithAlpha((byte)(0xFF * 0.2));
                canvas.DrawPath(path, paint);
                canvas.DrawPath(npath, paintDash);
                canvas.DrawLine(0, canvas.LocalClipBounds.Bottom - (float)MainQuotes.OrderByDescending(l => l.Close).ToList()[0].Close - 35, canvas.LocalClipBounds.Right, canvas.LocalClipBounds.Bottom - (float)MainQuotes.OrderByDescending(l => l.Close).ToList()[0].Close - 35, paintDot);
                canvas.DrawLine(0, canvas.LocalClipBounds.Bottom - (float)MainQuotes.OrderByDescending(l => l.Close).ToList()[MainQuotes.Count - 1].Close - 35, canvas.LocalClipBounds.Right, canvas.LocalClipBounds.Bottom - (float)MainQuotes.OrderByDescending(l => l.Close).ToList()[MainQuotes.Count - 1].Close - 35, paintDot);
                }

            if (SecondaryQuotes.Count != 0)
            {
                SKPath path = new SKPath();
                SKPath npath = new SKPath();

                double dsmax = SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[0].Close;
                double dsmin = SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[SecondaryQuotes.Count - 1].Close;

                path.MoveTo(0, canvas.LocalClipBounds.Bottom - 35);
                path.LineTo(0, canvas.LocalClipBounds.Bottom - (float)SecondaryQuotes[0].Close - 35);
                int last = 0;
                
                paint.Color = Color.Blue.ToSKColor();
                for (var i = 0; i < SecondaryQuotes.Count - 1; i++)
                {
                    path.LineTo(info.Width / SecondaryQuotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - (float)SecondaryQuotes[i + 1].Close - 35);
                    canvas.DrawLine(info.Width / SecondaryQuotes.Count * i, canvas.LocalClipBounds.Bottom - (float)SecondaryQuotes[i].Close - 35, info.Width / SecondaryQuotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - (float)SecondaryQuotes[i + 1].Close - 35, paint);
                    last = i + 1;

                    double current = SecondaryQuotes[i + 1].Close;

                    if (i % 3 == 0)
                    {
                        if (mmax <= smax)
                        {
                            npath.MoveTo(info.Width / MainQuotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - (float)SecondaryQuotes[i + 1].Close - 35);
                            npath.LineTo(info.Width / MainQuotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - 35);
                        }
                    }

                        if (current.Equals(dsmax) || current.Equals(dsmin))
                    {
                        canvas.DrawText(Convert.ToString(SecondaryQuotes[i + 1].Close), info.Width / SecondaryQuotes.Count * (i + 1), canvas.LocalClipBounds.Bottom - (float)SecondaryQuotes[i + 1].Close - 35, textPaint);
                    }
                }

                double first = SecondaryQuotes[0].Close;
                if (first.Equals(dsmax) || first.Equals(dsmin))
                {
                    canvas.DrawText(Convert.ToString(SecondaryQuotes[0].Close), info.Width / SecondaryQuotes.Count * (0), canvas.LocalClipBounds.Bottom - (float)SecondaryQuotes[0].Close - 35, textPaint);
                }
                path.LineTo(info.Width / SecondaryQuotes.Count * last, canvas.LocalClipBounds.Bottom - 35);
                path.Close();
                paint.Color = SKColors.Blue.WithAlpha((byte)(0xFF * 0.2));
                canvas.DrawPath(path, paint);
                paintDot.Color = Color.Black.ToSKColor();
                canvas.DrawLine(0, canvas.LocalClipBounds.Bottom - (float)SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[0].Close - 35, canvas.LocalClipBounds.Right, canvas.LocalClipBounds.Bottom - (float)SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[0].Close - 35, paintDot);
                canvas.DrawLine(0, canvas.LocalClipBounds.Bottom - (float)SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[SecondaryQuotes.Count - 1].Close - 35, canvas.LocalClipBounds.Right, canvas.LocalClipBounds.Bottom - (float)SecondaryQuotes.OrderByDescending(l => l.Close).ToList()[SecondaryQuotes.Count - 1].Close - 35, paintDot);
                canvas.DrawPath(npath, paintDash);
            }


        }

        
    }


}
