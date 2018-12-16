using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using xamarimCmovProj.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace xamarimCmovProj
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class DetailsPage : ContentPage
	{
        Company company;
        public String Go = "fofofo";
		public DetailsPage (Company c)
		{
			InitializeComponent ();

            Title = "Detalis of " + c.CompanyName;
            company = c;
            BindingContext = this;
            String tagsStr = "";
            for(int i =0; i < c.Tags.Count; i++)
            {
                tagsStr += c.Tags[i];
                if(i != c.Tags.Count -1)
                {
                    tagsStr += ", ";
                }
            }

            cName.Text = company.CompanyName;
            symbol.Text = company.Symbol;
            exchange.Text = company.Exchange;
            industry.Text = company.Industry;
            website.Text = company.Website;
            description.Text = company.Description;
            ceo.Text = company.CEO;
            issueType.Text = company.IssueType;
            sector.Text = company.Sector;
            tags.Text = tagsStr;
        }
	}
}