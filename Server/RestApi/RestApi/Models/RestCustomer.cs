using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace RestApi.Models
{
    public class RestCustomer
    {
        public long Id { get; set; }
        public string Name { get; set; }
        public string NIF { get; set; }
        public long RSA { get; set; }
    }
}
