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


        public MainPage()
        {
            InitializeComponent();
            RestService rest = new RestService();
            MainQuotes = rest.QuoteRefreshDataAsync().Result;
            System.Diagnostics.Debug.WriteLine(MainQuotes);
            

            Title = "Simple Circle";
            
            
            //SKCanvasView canvasView = new SKCanvasView();
            //canvasView.PaintSurface += OnCanvasViewPaintSurface;
            //Content = canvasView;

            
            //Companies.Add(rest.CompanyRefreshDataAsync().Result);
           

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
            //canvas.DrawCircle(info.Width / 2, info.Height / 2, 100, paint);

            if(MainQuotes.Count != 0)
            {

                SKPath path = new SKPath();


                float max = (float)MainQuotes.OrderByDescending(l => l.Close).ToList()[0].Close;
                float min = (float)MainQuotes.OrderByDescending(l => l.Close).ToList()[MainQuotes.Count - 1].Close;

                

                canvas.Scale(1, 8f, info.Width / MainQuotes.Count, min);

                canvas.DrawLine(0, canvas.LocalClipBounds.Bottom, info.Width, canvas.LocalClipBounds.Bottom, box);
                canvas.DrawLine(0, 0, 0, canvas.LocalClipBounds.Bottom, box);
                
                path.MoveTo(0, canvas.LocalClipBounds.Bottom);
                path.LineTo(0, (float)MainQuotes[0].Close);
                int last = 0;
                for (var i = 0; i < MainQuotes.Count - 1; i++)
                {
                    path.LineTo(info.Width / MainQuotes.Count * (i+1), (float)MainQuotes[i+1].Close);
                    canvas.DrawLine(info.Width / MainQuotes.Count * i, (float)MainQuotes[i].Close, info.Width / MainQuotes.Count * (i + 1), (float)MainQuotes[i + 1].Close, paint);
                    last = i+1;
                }
                path.LineTo(info.Width / MainQuotes.Count * last, canvas.LocalClipBounds.Bottom);
                path.Close();
                paint.Color = SKColors.Red.WithAlpha((byte)(0xFF * 0.2));
                canvas.DrawPath(path, paint);


            }

            
        }
    }


}
