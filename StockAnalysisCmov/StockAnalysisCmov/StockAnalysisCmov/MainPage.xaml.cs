using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using SkiaSharp;
using SkiaSharp.Views.Forms;
using Xamarin.Forms;

namespace StockAnalysisCmov
{
    public partial class MainPage : ContentPage
    {
        public MainPage()
        {
            InitializeComponent();

            Title = "Simple Circle";

            SKCanvasView canvasView = new SKCanvasView();
            canvasView.PaintSurface += OnCanvasViewPaintSurface;
            Content = canvasView;
        }

        void OnCanvasViewPaintSurface(object sender, SKPaintSurfaceEventArgs args)
        {
            SKImageInfo info = args.Info;
            SKSurface surface = args.Surface;
            SKCanvas canvas = surface.Canvas;

            canvas.Clear();

            SKPaint paint = new SKPaint
            {
                Style = SKPaintStyle.Stroke,
                Color = Color.Red.ToSKColor(),
                StrokeWidth = 25
            };
            //canvas.DrawCircle(info.Width / 2, info.Height / 2, 100, paint);

            
            //canvas.DrawCircle(info.Width / 2, info.Height / 2, 100, paint);
            var path = new SKPath();
            path.MoveTo(500, 500);
            path.LineTo(750, 1000);
            path.LineTo(1000, 500);
            //path.Close();
            canvas.DrawPath(path, paint);
        }
    }


}
